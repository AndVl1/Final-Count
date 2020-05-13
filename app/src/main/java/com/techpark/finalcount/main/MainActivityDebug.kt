package com.techpark.finalcount.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.techpark.finalcount.adding.views.activity.AddingActivity
import com.techpark.finalcount.auth.views.activity.AuthActivity
import com.techpark.finalcount.databinding.ActivityMainDebugBinding

class MainActivityDebug : AppCompatActivity() {
	private lateinit var mainActivityBinding: ActivityMainDebugBinding
	private val mAuth = FirebaseAuth.getInstance()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mainActivityBinding = ActivityMainDebugBinding.inflate(layoutInflater)
		setContentView(mainActivityBinding.root)

		if (mAuth.currentUser != null) {
			mainActivityBinding.mainText.text = mAuth.currentUser?.email ?: "no email"
			mainActivityBinding.logout.setOnClickListener {
				mAuth.signOut()
				LoginManager.getInstance().logOut()
				toAuthActivity()
			}
			mainActivityBinding.toAddActivity.setOnClickListener {
				startActivity(Intent(applicationContext, AddingActivity::class.java))
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
