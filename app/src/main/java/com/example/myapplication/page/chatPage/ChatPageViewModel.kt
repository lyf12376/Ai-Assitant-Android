package com.example.myapplication.page.chatPage

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Const.ModelList
import com.example.myapplication.Const.Token
import com.example.myapplication.Const.WebSocketUrl
import com.example.myapplication.database.chatHistory.ChatHistory
import com.example.myapplication.database.chatHistory.ChatHistoryDao
import com.example.myapplication.database.chatHistory.ChatMessage
import com.example.myapplication.network.chat.ChatResponse
import com.example.myapplication.network.chatList.ChatListService
import com.example.myapplication.network.eachChatRecord.Block
import com.example.myapplication.network.eachChatRecord.ChatRecordService
import com.example.myapplication.network.eachChatRecord.Message
import com.example.myapplication.network.uploadFile.UploadFileService
import com.example.myapplication.utils.AppUtils
import com.example.myapplication.utils.FilePathUtils
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
import okio.BufferedSink
import okio.ByteString
import okio.source
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChatPageViewModel @Inject constructor(
    private val uploadFileService: UploadFileService,
    private val chatRecordService: ChatRecordService,
    private val chatListService: ChatListService,
    private val chatHistoryDao: ChatHistoryDao,
) : ViewModel() {
    private val _chatList = MutableStateFlow<List<Message>>(emptyList())
    val chatList = _chatList.asStateFlow()

    private val originalChatListMessage = MutableStateFlow(Message("assistant", emptyList()))
    private val _end = MutableStateFlow(true)
    val end = _end.asStateFlow()

    private val webSocket = MutableStateFlow<WebSocket?>(null)

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private val _dialogContent = MutableStateFlow("")
    val dialogContent = _dialogContent.asStateFlow()

    /*
    Pair<String,String>
    前面代表文件名，后面代表类型，如Image，Document
    */
    private val _uploadList = MutableStateFlow(emptyList<Pair<String, String>>())
    val uploadList = _uploadList.asStateFlow()

    /*
    上传成功的文件
    Pair<String,String>
    前面代表图片的url或者是经过提取的文件的内容，后面代表类型，如Image，Document
    */
    private val _successfulUpload =
        MutableStateFlow(mutableMapOf<Int, List<Pair<String, String>>>())
    val successfulUpload = _successfulUpload.asStateFlow()

    /*
    0：正在上传
    1：上传成功
    2：上传失败
    */
    private val _fileStatus = MutableStateFlow(emptyList<Int>())
    val fileStatus = _fileStatus.asStateFlow()

    //上传文件列表的body列表，用于上传失败时重新上传
    private val bodyList = mutableListOf<MultipartBody.Part>()

    private val _currentModel = MutableStateFlow("")
    val currentModel = _currentModel.asStateFlow()

    //当前聊天记录id
    private val _chatId = MutableStateFlow(0)
    val chatId = _chatId.asStateFlow()
    private var newChat = false

    fun showDialog(content: String) {
        _showDialog.value = true
        _dialogContent.value = content
    }

    fun dismissDialog() {
        _showDialog.value = false
    }

//    init {
//
//    }

    fun getChatId(id: Int) {
        if (id == -1) {
            newChat = true
            generateChatId()
            initialWebSocket()
        } else {
            _chatId.value = id
            initChatRecord()
            initialWebSocket()
        }
    }

    private fun generateChatId() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val list = chatListService.getChatList(token = Token.TOKEN).data
                    val chatId = list[0].id + 1
                    _chatId.value = chatId
                    Log.d("TAG", "generateChatId: $chatId")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun initChatRecord() {
        Log.d("TAG", "initChatRecord: ${_chatId.value}")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val chatRecord = chatHistoryDao.getChatHistoryById(_chatId.value)
                    chatRecord?.content?.forEach {
                        _chatList.value += Message(it.role, it.content)
                        Log.d("TAG", "updateChatHistory: ${_chatList.value}")
                    }
                } catch (e: Exception) {
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

                webSocket.value = client.newWebSocket(request, object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        super.onOpen(webSocket, response)
                        // WebSocket连接成功，发送初始消息
                        if (newChat) {
                            val initialMessage = "{\"token\":\"$token\",\"id\":-1}"
                            webSocket.send(initialMessage)
                            newChat = false
                        } else {
                            val initialMessage = "{\"token\":\"$token\",\"id\":${_chatId}}"
                            webSocket.send(initialMessage)
                        }
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
                        Log.d("WebSocket已关闭:", "$code / $reason")
                        // 尝试重新连接
                        reconnect()
                    }

                    override fun onFailure(
                        webSocket: WebSocket,
                        t: Throwable,
                        response: Response?
                    ) {
                        super.onFailure(webSocket, t, response)
                        Log.d("tag", "WebSocket连接失败: ${t.message}")
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
                originalChatListMessage.value = Message(
                    "assistant",
                    listOf(
                        Block(
                            originalChatListMessage.value.content.toMutableList()[0].text + chatMessage.message,
                            0
                        )
                    )
                )
            else
                originalChatListMessage.value = Message(
                    "assistant",
                    originalChatListMessage.value.content + Block(chatMessage.message, 0)
                )
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
                    if (it.startsWith("```file")) {
                        if (it.split("\n")[1].split(".")[1].contains("png") || it.split("\n")[1].split(
                                "."
                            )[1].contains("jpg") || it.split("\n")[1].split(".")[1].contains("jpeg") || it.split(
                                "\n"
                            )[1].split(".")[1].contains("webp")
                        ) {
                            Block(text = it.split("\n")[2], type = 2)
                        } else
                            Block(text = it.removeSurrounding("```").trim(), type = 3)
                    } else if (it.startsWith("```")) {
                        Block(text = it.removeSurrounding("```").trim(), type = 1)
                    } else {
                        Block(text = it.trim(), type = 0)
                    }
                }
                Log.d("TAG", "handleIncomingMessage: $blockList")
                originalChatListMessage.value = Message("assistant", blockList)
                originalChatListMessage.value.content.forEach {
                    Log.d("TAG", "handleIncomingMessage: $it")
                }
                updateChatList()
                originalChatListMessage.value = Message("assistant", emptyList())
                _end.value = true
                updateChatHistory()
            }
        }
    }

    private fun extractBlocks(message: String): List<String> {
        val extractedBlocks = mutableListOf<String>()
        var codeBlock = false
        var fileBlock = false
        val lines = message.lines()
        var currentBlock = StringBuilder()

        for (line in lines) {
//            if (line.trim().startsWith("```file")) {
//                if (fileBlock) {
//                    // End of file block
//                    currentBlock.append(line).append("\n")
//                    extractedBlocks.add(currentBlock.toString().trim())
//                    currentBlock = StringBuilder()
//                } else {
//                    // Start of file block
//                    if (currentBlock.isNotEmpty()) {
//                        extractedBlocks.add(currentBlock.toString().trim())
//                        currentBlock = StringBuilder()
//                    }
//                    currentBlock.append(line).append("\n")
//                }
//                fileBlock = !fileBlock
//            }
//            else
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
        initialWebSocket()
    }

    fun send(message: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d("TAG", "send: ${_successfulUpload.value[_chatList.value.size - 1]}")
                val frontMessage =
                    _successfulUpload.value[_chatList.value.size - 1]?.joinToString("") {
                        "```file\n[[${it.first.split("/").last()}]]\n${it.first}\n```\n"
                    } ?: ""
                Log.d("TAG", "send: $frontMessage")
                val chat =
                    "{\"type\":\"chat\",\"message\":\"$frontMessage$message\",\"web\":false,\"model\":\"${ModelList.modelStoreMap[_currentModel.value]}\",\"context\":3,\"auto_use_coin\":true,\"ignore_context\":false,\"max_tokens\":2000,\"temperature\":0.6,\"top_p\":1,\"top_k\":5,\"presence_penalty\":0,\"frequency_penalty\":0,\"repetition_penalty\":1}"
                Log.d("TAG", "send: $chat")
                while (true) {
                    try {
                        val isSuccess = webSocket.value?.send(chat)
                        Log.d("TAG", "send: $isSuccess")
                        if (isSuccess == true)
                            break
                    } catch (e: Exception) {
                        e.printStackTrace()
                        reconnect()
                    }
                }

                val extractedBlocks = extractBlocks(frontMessage + message)
                val blockList = extractedBlocks.map {
                    if (it.startsWith("```file")) {
                        if (it.split("\n")[1].split(".")[1].contains("png") || it.split("\n")[1].split(
                                "."
                            )[1].contains("jpg") || it.split("\n")[1].split(".")[1].contains("jpeg") || it.split(
                                "\n"
                            )[1].split(".")[1].contains("webp")
                        ) {
                            Block(text = it.split("\n")[2], type = 2)
                        } else {
                            Block(text = it.split("\n")[1], type = 3)
                        }
                    } else if (it.startsWith("```")) {
                        Block(text = it.removeSurrounding("```").trim(), type = 1)
                    } else {
                        Block(text = it.trim(), type = 0)
                    }
                }
                _chatList.value += Message("user", blockList)
                updateChatHistory()
                _uploadList.value = emptyList()
                _successfulUpload.value = mutableMapOf()
                updateChatHistory()
            }
        }
    }

    fun uploadFile(uri: Uri, type: String, mode: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (mode == 1) {
                    // 处理从文件选择器获得的 URI
                    val contentResolver = AppUtils.getApp().applicationContext.contentResolver
                    val fileName = getFileName(contentResolver, uri)
                    val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"

                    try {
                        val requestBody = object : RequestBody() {
                            override fun contentType() = mimeType.toMediaTypeOrNull()

                            override fun writeTo(sink: BufferedSink) {
                                contentResolver.openInputStream(uri)?.use { inputStream ->
                                    sink.writeAll(inputStream.source())
                                }
                            }
                        }

                        _uploadList.value += Pair(fileName, type)
                        _fileStatus.value += 0

                        val body: MultipartBody.Part =
                            MultipartBody.Part.createFormData("file", fileName, requestBody)
                        bodyList.add(body)
                        val uploadResponse = uploadFileService.uploadFile(Token.TOKEN, body)
                        _successfulUpload.value[_chatList.value.size - 1] = emptyList()
                        Log.d("TAG", "uploadFile: ${uploadResponse.content}")
                        if (uploadResponse.status) {
                            val list = _fileStatus.value.toMutableList()
                            list[list.size - 1] = 1
                            _fileStatus.value = list.toList()
                            val l =
                                _successfulUpload.value[_chatList.value.size - 1]?.toMutableList()
                            l?.add(Pair(uploadResponse.content, type))
                            if (l != null) {
                                _successfulUpload.value[_chatList.value.size - 1] = l.toList()
                            }
                        } else {
                            val list = _fileStatus.value.toMutableList()
                            list[list.size - 1] = 2
                            _fileStatus.value = list.toList()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        val list = _fileStatus.value.toMutableList()
                        list[list.size - 1] = 2
                        _fileStatus.value = list.toList()
                    }
                } else {
                    val contentResolver = AppUtils.getApp().applicationContext.contentResolver
                    val inputStream = contentResolver.openInputStream(uri)

                    inputStream?.let {
                        // Try to get the original file name from the URI
                        val fileName = contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            cursor.moveToFirst()
                            cursor.getString(nameIndex)
                        }

                        // Extract the file extension if available
                        val fileExtension = fileName?.substringAfterLast('.', "")

                        // If no extension, default to jpg or handle according to file type
                        val extension = fileExtension ?: "jpg"

                        // Create a temporary file in the cache directory with the correct extension
                        val file = File(AppUtils.getApp().cacheDir, "temp_image.$extension")
                        file.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }

                        Log.d("TAG", "Temporary file path: ${file.absolutePath}")

                        // Check if the file is not empty
                        if (file.length() == 0L) {
                            Log.e("TAG", "The file is empty!")
                        }

                        // Try to get MIME type from ContentResolver
                        var mimeType = contentResolver.getType(uri)
                        Log.d("TAG", "MIME Type from ContentResolver: $mimeType")

                        if (mimeType == null) {
                            // Fallback to MimeTypeMap
                            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                            Log.d("TAG", "MIME Type from MimeTypeMap: $mimeType")
                        }

                        // If MIME type is still null, default to binary stream type
                        if (mimeType == null) {
                            mimeType = "application/octet-stream"
                            Log.d("TAG", "MIME Type defaulted: $mimeType")
                        }

                        val requestFile: RequestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())

                        _uploadList.value += Pair(file.name, type)
                        _fileStatus.value += 0
                        val body: MultipartBody.Part =
                            MultipartBody.Part.createFormData("file", file.name, requestFile)
                        bodyList.add(body)

                        try {
                            val uploadResponse = uploadFileService.uploadFile(Token.TOKEN, body)
                            Log.d("TAG", "uploadFile: $uploadResponse")
                            _successfulUpload.value[_chatList.value.size - 1] = emptyList()

                            if (uploadResponse.status) {
                                val list = _fileStatus.value.toMutableList()
                                list[list.size - 1] = 1
                                _fileStatus.value = list.toList()
                                val l =
                                    _successfulUpload.value[_chatList.value.size - 1]?.toMutableList()
                                l?.add(Pair(uploadResponse.content, type))
                                if (l != null) {
                                    _successfulUpload.value[_chatList.value.size - 1] = l.toList()
                                }
                                Log.d(
                                    "TAG",
                                    "uploadFile: ${_successfulUpload.value[_chatList.value.size - 1]}"
                                )
                            } else {
                                val list = _fileStatus.value.toMutableList()
                                list[list.size - 1] = 2
                                _fileStatus.value = list.toList()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            val list = _fileStatus.value.toMutableList()
                            list[list.size - 1] = 2
                            _fileStatus.value = list.toList()
                        }
                    } ?: run {
                        Log.e("TAG", "Failed to open input stream from URI")
                    }
                }

            }
        }
    }

    // 辅助函数：从 URI 获取文件名
    private fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
        var fileName = "unknown"
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }
        return fileName
    }

    fun cancelFile(index: Int) {
        viewModelScope.launch {
            val upload = _uploadList.value.toMutableList()
            val status = _fileStatus.value.toMutableList()
            upload.removeAt(index)
            status.removeAt(index)
            bodyList.removeAt(index)
            _uploadList.value = upload.toList()
            _fileStatus.value = status.toList()
        }
    }

    fun reUploadFile(index: Int) {
        viewModelScope.launch {
            val list = _fileStatus.value.toMutableList()
            list[index] = 0
            _fileStatus.value = list.toList()
            val body = bodyList[index]
            val uploadResponse = uploadFileService.uploadFile(Token.TOKEN, body)
            if (uploadResponse.status) {
                val list = _fileStatus.value.toMutableList()
                list[index] = 1
                _fileStatus.value = list.toList()
                val l = _successfulUpload.value[_chatList.value.size - 1]?.toMutableList()
                l?.add(Pair(uploadResponse.content, _uploadList.value[index].second))
                if (l != null) {
                    _successfulUpload.value[_chatList.value.size - 1] = l.toList()
                }
            } else {
                val list = _fileStatus.value.toMutableList()
                list[index] = 2
                _fileStatus.value = list.toList()
            }
        }
    }

    fun changeModel(model: String) {
        _currentModel.value = model
    }

    private fun updateChatHistory() {
        val chatList = _chatList.value
        Log.d("TAG", "-------------chatlist: $chatList")
        val chatMessage = chatList.map { message ->
            Log.d("TAG", "updateChatHistory: $message,${_currentModel.value}")
            ChatMessage(message.content, message.role)
        }
        Log.d("TAG", "-------------: $chatMessage")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    if (chatHistoryDao.getChatHistoryById(_chatId.value) == null)
                        chatHistoryDao.insertChatHistory(
                            ChatHistory(
                                _chatId.value,
                                _currentModel.value,
                                chatMessage
                            )
                        )
                    else
                        chatHistoryDao.updateChatHistory(_chatId.value, chatMessage)
                } catch (e: Exception) {
                    Log.d("database", "updateChatHistory: ${e.message}")
                }
            }
        }
    }

}
