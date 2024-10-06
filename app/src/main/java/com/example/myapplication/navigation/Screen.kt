package com.example.myapplication.navigation

sealed class Screen(val route:String, val description:String){
    data object RecordPage : Screen("RecordPage","主页面")

    data object ModelPage:Screen("ModelPage","模型选择页面")

    data object ChatPage : Screen("ChatPage","聊天页面")
}