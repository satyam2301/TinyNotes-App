package com.example.notesapp

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import com.example.notesapp.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        supportActionBar?.hide()

        binding.btnPasswordRecovery.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        val email = binding.etForgotEmail.text.toString()
        if (validateForm(email)) {
            showProgressBar()
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    hideProgressBar()
                    if (task.isSuccessful) {
                        binding.forgotEmail.visibility = View.GONE
                        binding.tvSubmitMsg.visibility = View.VISIBLE
                        binding.btnPasswordRecovery.visibility = View.GONE
                        showToast(applicationContext, "Succesfully send ")
                    } else {
                        showToast(this, task.exception!!.message.toString())
                    }
                }
        }
        else{
            showToast(this, "Enter Valid Email")
        }
    }

    private fun validateForm(email: String): Boolean {
        return when {
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
//                binding.forgotEmail.error = "Enter valid email"
                false
            }
            else -> {true}
        }
    }
}