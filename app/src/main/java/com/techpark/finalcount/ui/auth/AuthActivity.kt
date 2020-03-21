package com.techpark.finalcount.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.techpark.finalcount.MainActivity
import com.techpark.finalcount.databinding.ActivityAuthBinding
import com.techpark.finalcount.R

class AuthActivity : AppCompatActivity() {

    private lateinit var loginActivityBinding : ActivityAuthBinding
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(mAuth.currentUser != null) {
            toMainActivity()
            return
        }
        loginActivityBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(loginActivityBinding.root)

        loginActivityBinding.loginInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginActivityBinding.statusView.visibility = View.GONE
                loginActivityBinding.submitButton.isEnabled = s.toString().trim{ it <= ' '}.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })
        loginActivityBinding.authTypeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                loginActivityBinding.displayName.visibility = View.INVISIBLE
                loginActivityBinding.submitButton.setText(R.string.login)
            } else {
                loginActivityBinding.displayName.visibility = View.VISIBLE
                loginActivityBinding.submitButton.setText(R.string.register)
            }
        }
        loginActivityBinding.submitButton.setOnClickListener {
            authenticate(loginActivityBinding.loginInput.text.toString(),
                loginActivityBinding.passwordInput.text.toString(),
                loginActivityBinding.authTypeSwitch.isChecked)
        }
    }

    private fun authenticate(login: String, password: String, isLogin: Boolean): Boolean {
        if (!checkLogin(isLogin)) {
            loginActivityBinding.statusView.text = getString(R.string.invalid)
            loginActivityBinding.statusView.visibility = View.VISIBLE
            return false
        }
        if (isLogin){
            Log.d("LOGIN", "trying to login")
            loginWithEmail(login, password)
        } else{
            Log.d("REGISTER", "trying to register")
            registerWithEmail(login, password)
        }

        return true
    }

    private fun loginWithEmail(login: String, password: String){
        loginActivityBinding.progressBar.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(login, password)
            .addOnCompleteListener { task ->
            run {
                if (task.isSuccessful) {
                    toMainActivity()
                } else {
                    showError(task.exception!!.localizedMessage!!)
                    loginActivityBinding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun registerWithEmail(login: String, password: String){
        loginActivityBinding.progressBar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(login, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mAuth.currentUser!!.updateProfile(
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(loginActivityBinding.displayName.text.toString())
                        .build())
                mAuth.currentUser!!.sendEmailVerification()
                    .addOnCompleteListener{
                        Log.d("REGISTRATION", getString(R.string.verification))
                        Toast.makeText(applicationContext, getText(R.string.verification), Toast.LENGTH_LONG).show()
                        toMainActivity()
                    }.addOnFailureListener { Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show() }
            } else {
                showError(task.exception!!.localizedMessage!!)
                loginActivityBinding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showError(error: String) {
        loginActivityBinding.statusView.text = error
        loginActivityBinding.statusView.visibility = View.VISIBLE
    }

    private fun checkLogin(isLogin: Boolean): Boolean = if(isLogin) {
            !loginActivityBinding.loginInput.text.isNullOrEmpty() &&
                    !loginActivityBinding.passwordInput.text.isNullOrEmpty()
        } else !loginActivityBinding.loginInput.text.isNullOrEmpty() &&
                !loginActivityBinding.passwordInput.text.isNullOrEmpty() &&
                !loginActivityBinding.displayName.text.isNullOrEmpty()


    private fun toMainActivity() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }
}
