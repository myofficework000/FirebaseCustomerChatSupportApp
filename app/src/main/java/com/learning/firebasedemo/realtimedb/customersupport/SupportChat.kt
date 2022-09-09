package com.learning.firebasedemo.realtimedb.customersupport

data class SupportChat(var viewType: Int = 1, var text: String = " ")

data class SupportChatSenderData(
    var button1: String = " ",
    var button2: String = " ",
    var button3: String = " "
)
