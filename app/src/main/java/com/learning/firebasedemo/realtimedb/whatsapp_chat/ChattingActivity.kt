package com.learning.firebasedemo.realtimedb.whatsapp_chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.firebasedemo.databinding.ActivityChattingBinding

class ChattingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChattingBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chattingViewModel: ChattingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModelAndObservers()
        addMoreChat()
    }

    private fun setUpViewModelAndObservers() {
        chattingViewModel = ViewModelProvider(this)[ChattingViewModel::class.java]
        chattingViewModel.fetchNotes()
        chattingViewModel.allChats.observe(this) {
            chatAdapter = ChatAdapter(this@ChattingActivity, it.toMutableList())
            binding.apply {
                recyclerViewChat.layoutManager =
                    LinearLayoutManager(this@ChattingActivity)
                recyclerViewChat.adapter = chatAdapter
            }
        }
        addMoreChat()
    }


    private fun addMoreChat() {
        binding.apply {
            buttonReceiver.setOnClickListener {
                val chat = Chat(1, edtMessage.text.toString())
                chattingViewModel.addMoreChat(chat)
                edtMessage.text?.clear()
            }
            buttonSender.setOnClickListener {
                val chat = Chat(2, edtMessage.text.toString())
                chattingViewModel.addMoreChat(chat)
                edtMessage.text?.clear()
            }
        }
    }
}