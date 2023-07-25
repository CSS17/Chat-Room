package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var nickname:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enter.setOnClickListener {
            if (nick.text.isNotEmpty()) {
                nickname = nick.text.toString()
                val intent = Intent(this, ChatRoom::class.java)
                intent.putExtra("nickname", nickname)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Nickname cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }


    }
    }
