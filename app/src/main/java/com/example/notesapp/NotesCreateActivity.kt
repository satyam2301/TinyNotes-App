package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.example.notesapp.databinding.ActivityNotesCreateBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class NotesCreateActivity : BaseActivity() {
    private lateinit var binding : ActivityNotesCreateBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var db: FirebaseFirestore
    private lateinit var documentReference : DocumentReference
    private lateinit var noteID : String
    var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCreateNote)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth
        db = Firebase.firestore
        user = auth.currentUser!!


        val rTitle = intent.getStringExtra("title")
        val rContent = intent.getStringExtra("content")
        noteID = intent.getStringExtra("noteID").toString()

        if(noteID!="null" && noteID.isNotEmpty()){
            isEditMode = true
            Log.d("OUTPUTINTENT",noteID)
        }

        if(isEditMode == true){
            binding.pageTitle.text = "Edit your note."
            binding.deleteNoteFab.visibility = View.VISIBLE
            binding.createNoteTitle.setText(rTitle)
            binding.createNoteContent.setText(rContent)
        }

        binding.saveNoteFab.setOnClickListener {
            val title = binding.createNoteTitle.text.toString()
            val content = binding.createNoteContent.text.toString()
            if(title.isEmpty() || content.isEmpty()){
                showToast(this, "Both fields are required.")
            }else{
                val note = Note(title, content, Timestamp.now())
                saveNoteToFirebase(note)
            }
        }

        binding.deleteNoteFab.setOnClickListener {
            deleteNoteFromFirebase()
        }
    }

    private fun saveNoteToFirebase(note: Note) {
        documentReference = if(isEditMode){
            db.collection("notes")
                .document(user.uid)
                .collection("my_Notes")
                .document(noteID)
        }else{
            db.collection("notes")
                .document(user.uid)
                .collection("my_Notes")
                .document()
        }
        documentReference.set(note)
            .addOnCompleteListener {task->
                if(task.isSuccessful){
                    showToast(this, "Note added successfully !")
                    finish()
                }else{
                    showToast(this, "Failed while adding note!")
                }
            }

        Log.d("USERUID",user.uid)
    }

    private fun deleteNoteFromFirebase() {
        documentReference = db.collection("notes")
            .document(user.uid)
            .collection("my_Notes")
            .document(noteID)
        documentReference.delete()
            .addOnCompleteListener {task->
                if(task.isSuccessful){
                    showToast(this, "Note deleted successfully !")
                    finish()
                }else{
                    showToast(this, "Failed while deleting note!")
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}