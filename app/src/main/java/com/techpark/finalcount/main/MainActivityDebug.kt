package com.techpark.finalcount.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.techpark.finalcount.auth.views.activity.AuthActivity
import com.techpark.finalcount.data.PinPreferences
import com.techpark.finalcount.data.dbexpimp.JsonDbExportImportApiKt
import com.techpark.finalcount.databinding.ActivityMainDebugBinding
import com.techpark.finalcount.pincode.views.activity.PincodeActivity

class MainActivityDebug : AppCompatActivity() {
	private lateinit var mainActivityBinding: ActivityMainDebugBinding
	private val mAuth = FirebaseAuth.getInstance()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mainActivityBinding = ActivityMainDebugBinding.inflate(layoutInflater)
		setContentView(mainActivityBinding.root)

        if (mAuth.currentUser == null) {
            toAuthActivity()
        }

        mainActivityBinding.mainText.text = mAuth.currentUser?.email ?: "no email"
        mainActivityBinding.logout.setOnClickListener {
            mAuth.signOut()
            LoginManager.getInstance().logOut()
            PinPreferences(this).removePin()
            toAuthActivity()
        }
        mainActivityBinding.addPin.setOnClickListener {
            val intent = Intent(applicationContext, PincodeActivity::class.java)
            intent.putExtra("login", false)
            startActivity(intent)
        }
        mainActivityBinding.cancelPin.setOnClickListener {
            PinPreferences(this).removePin()
        }
    }

	private fun toAuthActivity() {
		startActivity(Intent(applicationContext, AuthActivity::class.java))
		finish()
	}
}
