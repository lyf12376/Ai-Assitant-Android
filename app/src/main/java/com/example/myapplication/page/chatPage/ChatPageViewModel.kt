package com.example.myapplication.page.chatPage

import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Const.Token
import com.example.myapplication.Const.WebSocketUrl
import com.example.myapplication.network.chat.ChatResponse
import com.example.myapplication.network.chatList.ChatListService
import com.example.myapplication.network.eachChatRecord.Block
import com.example.myapplication.network.eachChatRecord.ChatRecordService
import com.example.myapplication.network.eachChatRecord.Message
import com.example.myapplication.network.uploadFile.UploadFileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChatPageViewModel @Inject constructor(
    private val uploadFileService: UploadFileService,
    private val chatRecordService: ChatRecordService,
    private val chatListService: ChatListService
) : ViewModel() {

    private val _chatList = MutableStateFlow<List<Message>>(emptyList())
    val chatList = _chatList.asStateFlow()


    private val originalChatListMessage = MutableStateFlow(Message("assistant", emptyList()))
    private val _end = MutableStateFlow(true)
    val end = _end.asStateFlow()

    private val webSocket = MutableStateFlow<WebSocket?>(null)

    private val _isCodeBlock = MutableStateFlow(false)
    private val _tagNum = MutableStateFlow(0)

    init {
        //initChatRecord()
        initialWebSocket()
    }


    private fun initChatRecord(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val chatRecord = chatRecordService.getChatRecord(Token.TOKEN,47)
                    chatRecord.data.message.forEach { it ->
                        val extractedBlocks = extractBlocks(it.content)
                        val blockList = extractedBlocks.map {
                            if (it.startsWith("```")) {
                                Block(text = it.removeSurrounding("```").trim(), type = 1)
                            } else {
                                Block(text = it.trim(), type = 0)
                            }
                        }
                        _chatList.value += Message(it.role, blockList)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }

    }

    private fun initialWebSocket() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val client = OkHttpClient()
                val request: Request = Request.Builder()
                    .url(WebSocketUrl.URL)
                    .build()

                val token = Token.TOKEN
                val id = 45

                webSocket.value = client.newWebSocket(request, object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        super.onOpen(webSocket, response)
                        // WebSocket连接成功，发送初始消息
                        val initialMessage = "{\"token\":\"$token\",\"id\":$id}"
                        webSocket.send(initialMessage)
                    }

                    override fun onMessage(webSocket: WebSocket, text: String) {
                        super.onMessage(webSocket, text)
                        handleIncomingMessage(text)
                    }

                    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                        super.onMessage(webSocket, bytes)
                        println("收到二进制消息: ${bytes.hex()}")
                    }

                    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                        super.onClosing(webSocket, code, reason)
                        webSocket.close(1000, null)
                        println("WebSocket连接关闭: $code / $reason")
                    }

                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                        super.onClosed(webSocket, code, reason)
                        println("WebSocket已关闭: $code / $reason")
                        // 尝试重新连接
                        reconnect()
                    }

                    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                        super.onFailure(webSocket, t, response)
                        println("WebSocket连接失败: ${t.message}")
                        // 尝试重新连接
                        reconnect()
                    }
                })
            }
        }
    }

    private fun handleIncomingMessage(text: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val chatMessage = Json.decodeFromString<ChatResponse>(text)

            if (originalChatListMessage.value.content.isNotEmpty())
                originalChatListMessage.value = Message("assistant", listOf(Block(originalChatListMessage.value.content.toMutableList()[0].text + chatMessage.message,0)))
            else
                originalChatListMessage.value = Message("assistant",originalChatListMessage.value.content + Block(chatMessage.message,0))
            updateChatList()

            if (chatMessage.end) {
                val absoluteString = originalChatListMessage.value.content.joinToString("") {
                    it.text
                }
                val extractedBlocks = extractBlocks(absoluteString)
//                extractedBlocks.forEach {
//                    Log.d("TAG", "handleIncomingMessage: $it")
//                }
                val blockList = extractedBlocks.map {
                    if (it.startsWith("```")) {
                        Block(text = it.removeSurrounding("```").trim(), type = 1)
                    } else {
                        Block(text = it.trim(), type = 0)
                    }
                }
                originalChatListMessage.value = Message("assistant",blockList)
                originalChatListMessage.value.content.forEach {
                    Log.d("TAG", "handleIncomingMessage: $it")
                }
                updateChatList()
                originalChatListMessage.value = Message("assistant", emptyList())
                _end.value = true
            }
        }
    }

    private fun extractBlocks(message: String): List<String> {
        val extractedBlocks = mutableListOf<String>()
        var codeBlock = false
        val lines = message.lines()
        var currentBlock = StringBuilder()

        for (line in lines) {
            if (line.trim().startsWith("```")) {
                if (codeBlock) {
                    // End of code block
                    currentBlock.append(line).append("\n")
                    extractedBlocks.add(currentBlock.toString().trim())
                    currentBlock = StringBuilder()
                } else {
                    // Start of code block
                    if (currentBlock.isNotEmpty()) {
                        extractedBlocks.add(currentBlock.toString().trim())
                        currentBlock = StringBuilder()
                    }
                    currentBlock.append(line).append("\n")
                }
                codeBlock = !codeBlock
            } else {
                currentBlock.append(line).append("\n")
            }
        }

        if (currentBlock.isNotEmpty()) {
            extractedBlocks.add(currentBlock.toString().trim())
        }

        return extractedBlocks
    }


    private fun updateChatList() {
        if (_end.value) {
            val list = _chatList.value.toMutableList()
            list.add(originalChatListMessage.value)
            _chatList.value = list
            Log.d("TAG", "updateChatList: ${originalChatListMessage.value}")
            _end.value = false
        } else {
            val list = _chatList.value.toMutableList()
            list[list.size - 1] = originalChatListMessage.value
            _chatList.value = list
        }
    }

    private fun reconnect() {
        viewModelScope.launch {
            initialWebSocket()
        }
    }

    fun send(message:String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val chat =
                    "{\"type\":\"chat\",\"message\":\"$message\",\"web\":false,\"model\":\"gpt-3.5-turbo\",\"context\":3,\"auto_use_coin\":true,\"ignore_context\":false,\"max_tokens\":2000,\"temperature\":0.6,\"top_p\":1,\"top_k\":5,\"presence_penalty\":0,\"frequency_penalty\":0,\"repetition_penalty\":1}"
                try {
                    webSocket.value?.send(chat)
                }catch (e:Exception){
                    reconnect()
                }
                val extractedBlocks = extractBlocks(message)
                val blockList = extractedBlocks.map {
                    if (it.startsWith("```")) {
                        Block(text = it.removeSurrounding("```").trim(), type = 1)
                    } else {
                        Block(text = it.trim(), type = 0)
                    }
                }
                _chatList.value += Message("user", blockList)
            }
        }
    }

    fun uploadFile(file:File){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                withContext(Dispatchers.IO){
                    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(file.absolutePath)
                    val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
                    val requestFile: RequestBody = file.asRequestBody(mimeType?.toMediaTypeOrNull())
                    val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestFile)
                    uploadFileService.uploadFile(Token.TOKEN, body)
                }
            }
        }
    }

}