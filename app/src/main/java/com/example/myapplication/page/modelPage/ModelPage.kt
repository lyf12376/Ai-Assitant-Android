package com.example.myapplication.page.modelPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.Const.ModelList
import com.example.myapplication.R
import com.example.myapplication.database.chatHistory.ChatHistory
import com.example.myapplication.navigation.Screen
import com.example.myapplication.page.recordPage.ChatRecordItem
import com.example.myapplication.page.recordPage.RecordPageTopBar
import com.example.myapplication.ui.theme.LightModeColor

@Composable
fun ModelPage(navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LightModeColor.BackGroundColor)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(LightModeColor.TopBarColor)
            .statusBarsPadding()
        ) {
            IconButton(onClick = {navHostController.popBackStack()}, modifier = Modifier.align(Alignment.CenterVertically)) {
                Icon(
                    painterResource(R.drawable.back),
                    contentDescription = "",
                    modifier = Modifier.size(36.dp)
                )
            }

            Text("模型市场", fontSize = 24.sp, modifier = Modifier.align(Alignment.CenterVertically).padding(start = 8.dp))
        }
        LazyColumn (modifier = Modifier.weight(1f)){
            items(ModelList.modelStore.size) {
                ModelListItem(it){model->
                    navHostController.navigate(Screen.ChatPage.route+"/$model"+"/-1")
                }
            }
        }
    }
}

@Composable
fun ModelListItem(id:Int,chat:(model:String)->Unit) {
    val model = ModelList.modelStore[id]
    MaterialTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .clickable {
                    chat(model)
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Column {
                    Row {
                        Icon(
                            painterResource(id = ModelList.modelPictureMap[model]!!),
                            contentDescription = "chat record",
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                                .align(Alignment.CenterVertically),
                            tint = Color.Unspecified
                        )

                        Text(
                            text = ModelList.modelStore[id],
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 24.dp)
                                .weight(1f)
                        )
                    }
                    Text(ModelList.describeMap[model]!!, modifier = Modifier.padding(8.dp))
                }

            }

        }
    }
}