package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chatapp.SocketHandler.closeConnection
import kotlinx.android.synthetic.main.activity_chat_room.*

class ChatRoom : AppCompatActivity() {
    private var text: String = ""
    private var onlineUser: Int = 0 // Online kullanıcı sayısını takip edeceğimiz değişken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        val nickname = intent.getStringExtra("nickname")

        SocketHandler.setSocket()
        val mSocket = SocketHandler.getSocket()
        mSocket.connect()

        // Online kullanıcı sayısını takip etmek için olay dinleyicisini başlatıyoruz
        mSocket.on("onlineUser") { args ->
            if (args.isNotEmpty()) {
                onlineUser = args[0] as Int
                runOnUiThread {
                    online_user_count.text = "Online Users: $onlineUser"
                }
            }
        }

        send_btn.setOnClickListener {
            Log.d("TAG", editText.text.toString())
            text = editText.text.toString()
            editText.setText("")
            mSocket.emit("counter", nickname, text)
            scrollChatToBottom()
        }

        mSocket.on("counter") { args, ->
            Log.d("TAG", args.size.toString())
            if (args.isNotEmpty()) {
                Log.d("TAG", args[0].toString())
                val message = args[0] as String
                val name = args[1] as String
                runOnUiThread {
                    val currentText = chat.text.toString()
                    val newText = "$name: $message\n"
                    chat.text = currentText + newText
                    scrollChatToBottom()
                }
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
