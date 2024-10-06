package com.example.myapplication.page.recordPage

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.database.chatHistory.ChatHistory
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.LightModeColor

@Composable
fun RecordPage(navHostController: NavHostController,recordViewModel: RecordViewModel = hiltViewModel()) {
    val list by recordViewModel.conversationHistory.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LightModeColor.BackGroundColor)
    ) {
        RecordPageTopBar()
        LazyColumn (modifier = Modifier.weight(1f)){
            Log.d("TAG", "RecordPage: $list")
            if (list.isNotEmpty()) {
                items(list.size) { index ->
                    Log.d("TAG", "Rendering item at index $index")
                    ChatRecordItem(list[index]) {
                        navHostController.navigate(Screen.ChatPage.route + "/${list[index].model+"/${it}"}")
                    }
                }
            } else {
                Log.d("TAG", "List is empty, no items to render")
            }
        }
        Button(onClick = {navHostController.navigate(Screen.ModelPage.route)}, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .navigationBarsPadding(), shape = RoundedCornerShape(8.dp)) {
            Text("Add a new chat")
        }
    }
}


@Composable
fun ChatRecordItem(chatHistory: ChatHistory,navigate:(chatId:Int)->Unit) {
    val first = chatHistory.content[0].content.filter {
        it.type == 0
    }[0].text
    MaterialTheme{
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .clickable { navigate(chatHistory.id) },
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.chat_record),
                    contentDescription = "chat record",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                        .align(Alignment.CenterVertically)
                )

                Text(
                    text = first,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,  // 限制为一行
                    overflow = TextOverflow.Ellipsis,  // 超出部分显示省略号
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 24.dp)
                        .weight(1f)
                )

            }
        }
    }

}

@Composable
@Preview
fun RecordPageTopBar() {
    Row(
        Modifier
            .fillMaxWidth()
            .background(LightModeColor.TopBarColor)
            .statusBarsPadding()
            .padding(top = 8.dp)
    ) {
        Text(
            text = "YiChat",
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }

}