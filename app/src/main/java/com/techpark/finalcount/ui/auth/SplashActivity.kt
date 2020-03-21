package com.techpark.finalcount.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.techpark.finalcount.MainActivity


class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(applicationContext,  AuthActivity::class.java))
        } else {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }
}