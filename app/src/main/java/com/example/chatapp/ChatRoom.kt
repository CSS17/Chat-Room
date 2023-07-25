package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.SocketHandler.closeConnection
import kotlinx.android.synthetic.main.activity_chat_room.*

class ChatRoom : AppCompatActivity() {
    private var message: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        val nickname = intent.getStringExtra("nickname").toString()

        val viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        // Online kullanıcı sayısını takip etmek için olay dinleyicisini başlatıyoruz
        viewModel.showOnlineUser { onlineUser ->
            runOnUiThread {
                online_user_count.text = "Online Users: $onlineUser"
            }
        }

        send_btn.setOnClickListener {
            Log.d("TAG", editText.text.toString())
            message = editText.text.toString()
            editText.setText("")
            viewModel.sendMessage(nickname, message)
            scrollChatToBottom()
        }

        viewModel.showReceivedMessage { name, message ->
            runOnUiThread {
                val currentText = chat.text.toString()
                val newText = "$name: $message\n"
                chat.text = currentText + newText
                scrollChatToBottom()
            }
        }
    }

    private fun scrollChatToBottom() {
        chat.post {
            val scrollAmount = chat.layout.getLineTop(chat.lineCount) - chat.height
            if (scrollAmount > 0) {
                chat.scrollTo(0, scrollAmount)
            } else {
                chat.scrollTo(0, 0)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        closeConnection()
    }
}


