package com.example.chatapp

import android.view.textclassifier.ConversationActions.Message
import androidx.lifecycle.ViewModel
import io.socket.client.Socket


class ChatViewModel : ViewModel() {
    private var onlineUser: Int = 0
    var mSocket: Socket

    init {
        SocketHandler.setSocket()
        mSocket = SocketHandler.getSocket()
        mSocket.connect()
    }

    fun showOnlineUser(callback: (Int) -> Unit) {
        mSocket.on("onlineUser") { args ->
            if (args.isNotEmpty()) {
                onlineUser = args[0] as Int
                callback(onlineUser)
            }
        }
    }

    fun showReceivedMessage(callback: (String, String) -> Unit) {
        mSocket.on("counter") { args ->
            if (args.isNotEmpty()) {
                val message = args[0] as String
                val name = args[1] as String
                callback(name, message)
            }
        }
    }

    fun sendMessage(nickname:String,message:String){
        mSocket.emit("counter",nickname,message)
    }
}







