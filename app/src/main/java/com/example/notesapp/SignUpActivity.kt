package com.example.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.example.notesapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : BaseActivity() {
    private lateinit var signUpBinding: ActivitySignUpBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signUpBinding.root)

        supportActionBar?.hide()

//        get instance in auth variable
        auth = Firebase.auth

        signUpBinding.tvLogin.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        signUpBinding.btnSignUp.setOnClickListener {
            userRegistration()
        }
    }

    private fun userRegistration() {
        val email = signUpBinding.signUpEmail.text.toString()
        val password = signUpBinding.signUpPassword.text.toString()
        val confirmPassword = signUpBinding.signUpConfirmPassword.text.toString()
        if(validateForm(email,password,confirmPassword)){
            showProgressBar()
//            store user in firebase
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {task->
                    if(task.isSuccessful){
                        hideProgressBar()
                        showToast(this,"You are successfully registered.")
//                        to verify user, that he or she is valid or not
                        auth.currentUser?.sendEmailVerification()
                        auth.signOut()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }else {
                        showToast(this, task.exception!!.message.toString() )
                        hideProgressBar()
                    }
                }
        }
    }


    private fun validateForm(email: String, password: String, confirmPassword : String): Boolean {
        return when {
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                signUpBinding.tilEmail.error = "Enter valid email"
                false
            }
            TextUtils.isEmpty(password)->{
                signUpBinding.tilPassword.error = "Enter Passsword"
                false
            }
            TextUtils.isEmpty(confirmPassword)->{
                signUpBinding.tilConfirmPassword.error = "Enter Passsword to Confirm"
                false
            }
            (password.length < 8 )->{
                signUpBinding.tilPassword.error = "Password should be greater than 8"
                false
            }
            !password.equals(confirmPassword)->{
                signUpBinding.tilConfirmPassword.error = "Password does not match"
                false
            }
            else -> {
                true
            }
        }
    }
}