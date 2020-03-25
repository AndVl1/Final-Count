package com.techpark.finalcount.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.techpark.finalcount.auth.views.activity.AuthActivity
import com.techpark.finalcount.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var mainActivityBinding: ActivityMainBinding
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        if (mAuth.currentUser != null) {
            mainActivityBinding.mainText.text = mAuth.currentUser!!.isEmailVerified.toString()
            mainActivityBinding.logout.setOnClickListener {
                mAuth.signOut()
                LoginManager.getInstance().logOut()
                toAuthActivity()
            }
        } else {
            toAuthActivity()
        }
    }

    private fun toAuthActivity() {
        startActivity(Intent(applicationContext, AuthActivity::class.java))
        finish()
    }
}
