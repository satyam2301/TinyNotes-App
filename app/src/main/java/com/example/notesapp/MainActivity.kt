package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.databinding.ActivityMainBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var user : FirebaseUser
    private lateinit var db : FirebaseFirestore
    private lateinit var query: Query
    lateinit var options : FirestoreRecyclerOptions<Note>
    lateinit var noteAdapter : NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        user = auth.currentUser!!
        db = Firebase.firestore

        recyclerView = binding.rcView

        binding.innerFab.setOnClickListener {
            startActivity(Intent(this, NotesCreateActivity::class.java))
        }

        binding.menuBtn.setOnClickListener {
            showMenu()
        }
        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        query = db.collection("notes")
            .document(user.uid)
            .collection("my_Notes")
            .orderBy("timestamp",Query.Direction.DESCENDING)
        options = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query,Note::class.java)
            .build()

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        noteAdapter = NoteAdapter(options,this)
        recyclerView.adapter = noteAdapter

    }

    private fun showMenu() {
        val popupMenu = PopupMenu(this,binding.menuBtn)
        popupMenu.menu.add("Logout")
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener {menuItem->
            if(menuItem.title == "Logout"){
                auth.signOut()
                startActivity(Intent(this,LoginActivity::class.java))
                true
            }
            else {false}
        }
    }

    override fun onStart() {
        super.onStart()
        noteAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        noteAdapter.stopListening()
    }

    override fun onResume() {
        super.onResume()
        noteAdapter.notifyDataSetChanged()
    }

}

