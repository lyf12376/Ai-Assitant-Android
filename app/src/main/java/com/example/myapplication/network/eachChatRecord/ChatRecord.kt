package com.example.myapplication.network.eachChatRecord

data class ChatRecord(
    val data: Data,
    val message: String,
    val status: Boolean
)

data class Data(
    val auth: Boolean,
    val user_id: Int,
    val id: Int,
    val name: String,
    val message: List<EachMessage>,
    val model: String,
    val enable_web: Boolean,
    val shared: Boolean,
    val context: Int,
    val auto_use_coin: Boolean
)

data class EachMessage(
    val role: String,
    val content: String
)

data class Message(
    val role: String,
    val content: List<Block>
)

data class Block(
    val text: String,
    val type:Int
    //0代表文本，
    //1代表代码块
    //2代表图片
    //3代表其他文件
)
