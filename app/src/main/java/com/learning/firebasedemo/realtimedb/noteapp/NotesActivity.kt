package com.learning.firebasedemo.realtimedb.noteapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.learning.firebasedemo.databinding.ActivityNotesBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotesBinding
    private lateinit var ref: DatabaseReference
    private lateinit var noteAdapter: NoteAdapter
    private var items: MutableList<Notes> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        ref = FirebaseDatabase.getInstance().getReference("Notes")
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@NotesActivity)
            btnSave.setOnClickListener {
                saveNotes(edtTitle.text.toString(), edtDesc.text.toString())
            }
        }
        fetchNotes()
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveNotes(title: String, desc: String) {
        val date = SimpleDateFormat("dd/MMM/yyy").format(Date())
        val noteId = ref.push().key.toString()

        val note = Notes(title, desc, date)
        ref.child(noteId).setValue(note)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.edtTitle.text?.clear()
                    binding.edtDesc.text?.clear()
                    makeToast("added!!")
                    fetchNotes()
                } else {
                    makeToast("Failed to add")
                }
            }
    }

    private fun fetchNotes() {
        binding.circularProgressBar.visibility = View.VISIBLE

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    items.clear()
                    for (h in snapshot.children) {
                        val notes = h.getValue(Notes::class.java)
                        items.add(notes!!)
                    }
                    noteAdapter = NoteAdapter(items as ArrayList<Notes>)
                    binding.recyclerView.adapter = noteAdapter
                    binding.circularProgressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun makeToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}