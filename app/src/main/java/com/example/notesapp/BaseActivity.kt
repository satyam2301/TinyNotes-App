package com.example.notesapp

import android.app.Activity
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import java.util.*

open class BaseActivity : AppCompatActivity() {
    private lateinit var pb : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

    }

    fun showProgressBar(){
        pb = Dialog(this)
        pb.setContentView(R.layout.progress_bar)
        pb.setCancelable(false)
        pb.show()
    }

    fun hideProgressBar(){
        pb.hide()
    }

    fun showToast(context: Context, msg:String){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
    }

    fun getRandomColor(): Int {
        val colorcode = listOf(
            R.color.gray,
            R.color.pink,
            R.color.lightgreen,
            R.color.skyblue,
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4,
            R.color.color5,
            R.color.green
        )
        val random = Random()
        val number = random.nextInt(colorcode.size)
        return colorcode[number]
    }
}