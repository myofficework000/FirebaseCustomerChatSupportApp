package com.learning.firebasedemo.realtimedb.whatsapp_chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*

class ChattingViewModel : ViewModel() {
    private var ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("CHAT")
    private var items: MutableList<Chat> = mutableListOf()
    val allChats = MutableLiveData<List<Chat>>()

    fun fetchNotes() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    items.clear()
                    for (h in snapshot.children) {
                        val chat = h.getValue(Chat::class.java)
                        items.add(chat!!)
                    }
                    allChats.postValue(items)
                }
            }
        })
    }

    fun addMoreChat(chat: Chat) {
        val noteId = ref.push().key.toString()
        ref.child(noteId).setValue(chat).addOnCompleteListener {
            fetchNotes()
        }
    }
}