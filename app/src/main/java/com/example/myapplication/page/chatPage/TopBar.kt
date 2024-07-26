package com.example.myapplication.page.chatPage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Const.ModelList
import com.example.myapplication.Const.ScreenParam
import com.example.myapplication.R
import com.example.myapplication.ui.theme.LightModeColor
import com.example.myapplication.utils.ScreenConstrainUtils

@Composable
fun TopBar(currentModel: String, onModelChange: (String) -> Unit) {
    ScreenConstrainUtils.getScreenWidthHeight()
    ScreenConstrainUtils.getScreenMetrics()
    ScreenParam.logAll()
    val iconSize = 36.dp
    var showBar by remember {
        mutableStateOf(false)
    }

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
                    .size(iconSize)
                    .clickable {

                    }
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = currentModel,
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
                    .size(iconSize)
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
                        ModelList.items.forEach {
                            modelItem(modifier = Modifier, it, onclick = onModelChange){
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
fun modelItem(modifier: Modifier, text: String, onclick:(select:String)->Unit, selectFinish:()->Unit) {
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