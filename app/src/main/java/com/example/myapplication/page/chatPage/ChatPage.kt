package com.example.myapplication.page.chatPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.R
import com.example.myapplication.network.eachChatRecord.Block
import com.example.myapplication.utils.ClipBoardUtils
import com.example.myapplication.utils.CodeBlock
import com.example.myapplication.utils.highlightSyntax
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatPage(chatPageViewModel: ChatPageViewModel = hiltViewModel()) {
    val chatList by chatPageViewModel.chatList.collectAsState()
    var currentMessage by remember { mutableStateOf(TextFieldValue("")) }
    val scope = rememberCoroutineScope()

    fun sendMessage() {
        if (currentMessage.text.isNotEmpty()) {
            chatPageViewModel.send(currentMessage.text)
            scope.launch {
                currentMessage = TextFieldValue("")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(chatList.size) { index ->
                val message = chatList[index]
                if (message.role == "assistant")
                    AiMessage(message.content)
                else
                    UserMessage(message.content)
            }
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = currentMessage,
                onValueChange = { currentMessage = it },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(1f),
                maxLines = 4,
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (currentMessage.text.isEmpty()) {
                                Text("Enter your message", color = Color.Gray)
                            }
                            innerTextField()
                        }
                        IconButton(
                            onClick = {
                                sendMessage()
                            },
                        ) {
                            Icon(Icons.Filled.Send, null)
                        }
                    }
                }
            )
        }
        Row {

        }
    }
}


@Composable
fun UserMessage(message: List<Block>) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize(align = Alignment.TopCenter)
    ) {
        IconWithBackground(true)
        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            message.forEach {
                if (it.type == 0){
                    MessageText(it.text)
                }else if (it.type == 1) {
                    val language = it.text.split("\n")[0]
                    val code = it.text.removeRange(0, language.length)
                    Code(code, language)
                }
            }
        }
    }
}


@Composable
fun AiMessage(message: List<Block>) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize(align = Alignment.TopCenter)
    ) {
        IconWithBackground(false)
        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            message.forEach {
                if (it.type == 0){
                    MessageText(it.text)
                }else if (it.type == 1) {
                    val language = it.text.split("\n")[0]
                    val code = it.text.removeRange(0, language.length)
                    Code(code, language)
                }
            }
        }
    }
}

@Composable
fun IconWithBackground(isUser:Boolean,modifier: Modifier = Modifier) {
    Icon(
        painterResource(id = if (isUser) R.drawable.user else R.drawable.openai),
        tint = Color.Unspecified,
        contentDescription = "Icon",
        modifier = modifier
            .size(48.dp)
            .padding(4.dp)
            .background(
                color = Color("#10A37F".toColorInt()),
                shape = CircleShape
            )
            .padding(4.dp)
    )
}

@Composable
fun MessageText(text: String,modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(12.dp)
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif
        )
    }
}

@Composable
fun Code(code: String, language: String) {
    val context = LocalContext.current
    var isCopied by remember {
        mutableStateOf(false)
    }
    val circleSize = 20.dp
    val iconSize = 20.dp
    LaunchedEffect (isCopied){
        if (isCopied){
            delay(5000)
            isCopied = false
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(12.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.onSurface
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column (Modifier.weight(1f)){
                    CircleShapeBox(Color.Red, circleSize,
                        Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
                Column (Modifier.weight(1f)){
                    CircleShapeBox(Color.Yellow, circleSize,
                        Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
                Column (Modifier.weight(1f)){
                    CircleShapeBox(Color.Green, circleSize,
                        Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }

                Row(modifier = Modifier.weight(4f)) {
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Icon(
                            painter = painterResource(id = if (!isCopied) R.drawable.copy else R.drawable.copy_successful),
                            contentDescription = "Copy",
                            modifier = Modifier
                                .size(iconSize)
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    ClipBoardUtils.copyTextToClipboard(context, code)
                                    isCopied = true
                                },
                            tint = Color.Unspecified
                        )
                        Text(
                            text = language,
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            CodeBlockComponent(
                codeBlock = CodeBlock(
                    language = language,
                    code = code
                )
            )
        }
    }
}

@Composable
fun CircleShapeBox(color: Color, size: Dp,modifier: Modifier) {
    Box(
        modifier = modifier
            .size(size)
            .background(color, shape = CircleShape)
    )
}

@Composable
fun CodeBlockComponent(codeBlock: CodeBlock) {
    val highlightedCode = highlightSyntax(codeBlock.code, codeBlock.language)
    LazyRow {
        item {
            Text(
                text = highlightedCode,
                modifier = Modifier.fillMaxWidth(),
                fontFamily = FontFamily.Monospace,
                fontSize = 18.sp
            )
        }
    }
}



@Composable
@Preview
fun ChatScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

    }
}

