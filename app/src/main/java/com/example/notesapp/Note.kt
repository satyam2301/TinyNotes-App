package com.example.notesapp

import com.google.firebase.Timestamp


data class Note(
    var title : String,
    var content : String,
    var timestamp : Timestamp
){
    constructor():this("","", Timestamp.now())
}
