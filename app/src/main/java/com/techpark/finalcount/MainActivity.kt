package com.techpark.finalcount

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.techpark.finalcount.databinding.ActivityMainBinding
import com.techpark.finalcount.ui.auth.AuthActivity

class MainActivity : AppCompatActivity() {
    lateinit var mainActivityBinding: ActivityMainBinding
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        mainActivityBinding.mainText.text = mAuth.currentUser!!.isEmailVerified.toString()
        mainActivityBinding.logout.setOnClickListener {
            mAuth.signOut()
            LoginManager.getInstance().logOut()
            startActivity(Intent(applicationContext, AuthActivity::class.java))
            finish()
        }

        mainActivityBinding.resend.setOnClickListener {
            mAuth.currentUser!!.sendEmailVerification()
                .addOnCompleteListener{
                    Log.d("VERIFICATION", getString(R.string.verification))
                    Toast.makeText(applicationContext, getText(R.string.verification), Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Log.d("VERIFICATION", it.localizedMessage!!)
                    Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }
    }
}
