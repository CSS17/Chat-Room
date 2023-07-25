package com.example.chatapp


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.socket.client.Socket
import io.socket.emitter.Emitter

class ChatViewModel : ViewModel() {

    private val _chatMessages = MutableLiveData<String>()
    val chatMessages: LiveData<String>
        get() = _chatMessages

    private var mSocket: Socket? = null // Nullable Socket türünde değişken

    init {
        mSocket = SocketHandler.getSocket() // mSocket'ı burada başlatıyoruz
        observeSocketEvents()
    }

    private fun observeSocketEvents() {
        mSocket?.on("counter", onCounterEvent)
    }

    private val onCounterEvent = Emitter.Listener { args ->
        if (args.isNotEmpty()) {
            val message = args[0] as String
            val name = args[1] as String
            val currentMessages = _chatMessages.value ?: ""
            val newMessage = "$name: $message\n"
            _chatMessages.postValue(currentMessages + newMessage)
        }
    }

    fun sendMessage(nickname: String, message: String) {
        mSocket?.emit("counter", nickname, message)
    }

}





