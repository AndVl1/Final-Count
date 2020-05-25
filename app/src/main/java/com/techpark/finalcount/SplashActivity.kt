package com.techpark.finalcount

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.techpark.finalcount.auth.views.activity.AuthActivity
import com.techpark.finalcount.data.PinPreferences
import com.techpark.finalcount.main.views.activity.MainActivity
import com.techpark.finalcount.pincode.views.activity.PincodeActivity


class SplashActivity: AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		if (FirebaseAuth.getInstance().currentUser == null) {
			startActivity(Intent(applicationContext,  AuthActivity::class.java))
		} else {
			if (!PinPreferences(this).hasPin()) {
				startActivity(Intent(applicationContext, MainActivity::class.java))
			}
			else {
				val intent = Intent(applicationContext, PincodeActivity::class.java)
				intent.putExtra("login", true)
				startActivity(intent)
			}
		}
	}
}
