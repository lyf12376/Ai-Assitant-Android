package com.example.myapplication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Const.ScreenParam
import com.example.myapplication.ui.theme.LightModeColor
import com.example.myapplication.utils.ScreenConstrainUtils

@Composable
@Preview
fun InputTextField() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightModeColor.BackGroundColor)
    ) {
        var currentMessage by remember { mutableStateOf(TextFieldValue("")) }

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
                            },
                        ) {
                            Icon(Icons.Filled.Send, null)
                        }
                    }
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                },
                modifier = Modifier
                    .padding(8.dp)
                    .height(36.dp)
                    .weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = {
                },
                modifier = Modifier
                    .padding(8.dp)
                    .height(36.dp)

                    .weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.picture),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = {
                },
                modifier = Modifier
                    .padding(8.dp)
                    .height(36.dp)
                    .weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.document),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun TopBarTest() {
    ScreenConstrainUtils.getScreenWidthHeight()
    ScreenConstrainUtils.getScreenMetrics()
    ScreenParam.logAll()
    var showBar by remember {
        mutableStateOf(false)
    }
    var model by remember {
        mutableStateOf("gpt-4o")
    }
    val items = listOf(
        "gpt-4o",
        "kimi",
        "gpt-4o-mini",
        "claude-3.5-sonnet",
        "claude-3-haiku",
        "gpt-4o-all",
        "gpt-4-turbo",
        "more model"
    )
    val paddings = 8f
    val strokeWidth = 4f
    Column(
        modifier = Modifier
            .background(LightModeColor.BackGroundColor)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightModeColor.TopBarColor)
        ) {
            Icon(
                painterResource(id = R.drawable.record),
                contentDescription = "model",
                modifier = Modifier
                    .padding(12.dp)
                    .size(48.dp)
                    .clickable {
                        showBar = !showBar
                    }
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = model,
                fontSize = 24.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(
                        PaddingValues(8.dp)
                    )
            )
            Icon(
                painterResource(id = R.drawable.model),
                contentDescription = "model",
                modifier = Modifier
//                    .drawBehind {
//                        drawLine(
//                            color = Color.Black,
//                            start = Offset(0f + paddings, 0f + paddings),
//                            end = Offset(0f + paddings, size.height - paddings),
//                            strokeWidth = strokeWidth
//                        )
//                        drawLine(
//                            color = Color.Black,
//                            start = Offset(0f + paddings, 0f + paddings),
//                            end = Offset(size.width - paddings, 0f + paddings),
//                            strokeWidth = strokeWidth
//                        )
//                        drawLine(
//                            color = Color.Black,
//                            start = Offset(size.width - paddings, 0f + paddings),
//                            end = Offset(size.width - paddings, size.height - paddings),
//                            strokeWidth = strokeWidth
//                        )
//                        drawLine(
//                            color = Color.Black,
//                            start = Offset(0f + paddings, size.height - paddings),
//                            end = Offset(size.width - paddings, size.height - paddings),
//                            strokeWidth = strokeWidth
//                        )
//                    }
                    .padding(12.dp)
                    .size(48.dp)
                    .clickable {
                        showBar = !showBar
                    }
                    .align(Alignment.CenterVertically)
            )
        }
        Divider()
        AnimatedVisibility(visible = showBar) {
            Box(Modifier.fillMaxWidth()) {
                Row(Modifier.align(Alignment.CenterEnd)) {
                    Column(
                        Modifier.background(
                            MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(8.dp)
                        )
                    ) {
                        items.forEach {
                            modelItem(modifier = Modifier, it,{select->
                                model = select
                            }){
                                showBar = !showBar
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun modelItem(modifier: Modifier, text: String,onclick:(select:String)->Unit,selectFinish:()->Unit) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontFamily = FontFamily.Monospace,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                PaddingValues(8.dp)
            )
            .clickable {
                onclick(text)
                selectFinish()
            }
    )
}