package com.techpark.finalcount

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.techpark.finalcount.auth.views.activity.AuthActivity
import com.techpark.finalcount.main.MainActivityDebug


class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(applicationContext,  AuthActivity::class.java))
        } else {
            startActivity(Intent(applicationContext, MainActivityDebug::class.java))
        }
    }
}