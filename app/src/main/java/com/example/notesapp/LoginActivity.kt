package com.example.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import com.example.notesapp.databinding.ActivityLoginBinding
import com.example.notesapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : BaseActivity() {

    private lateinit var signinBinding: ActivityLoginBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signinBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(signinBinding.root)

        auth = Firebase.auth

        supportActionBar?.hide()

        signinBinding.forgotPassword.setOnClickListener {
            val intent = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        signinBinding.tvRegister.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

        signinBinding.btnSignIn.setOnClickListener {
            userLogin()
        }

    }

    private fun userLogin() {
        val email = signinBinding.SignInEmail.text.toString()
        val password = signinBinding.loginPassword.text.toString()

        if(validateForm(email,password)){
            showProgressBar()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        hideProgressBar()
                        if(auth.currentUser?.isEmailVerified == true){
                            showToast(this,"Logged In Successfully !!!")
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        else{
                            showToast(this,"EMail is not Verified. Please Verify it.")
                            auth.signOut()
                        }
                    }else{
                        hideProgressBar()
                        showToast(this,task.exception?.message.toString())
                    }
                }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                signinBinding.tilEmail.error = "Enter valid email"
                false
            }
            TextUtils.isEmpty(password)->{
                signinBinding.tilPassword.error = "Enter Passsword"
                false
            }
            else -> {true}
        }
    }

}