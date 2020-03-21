package com.techpark.finalcount.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.techpark.finalcount.MainActivity
import com.techpark.finalcount.R
import com.techpark.finalcount.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var loginActivityBinding : ActivityAuthBinding
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

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


    // ----------------- GOOGLE -----------------
    fun loginWithGoogle(view: View){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("GOOGLE", "Google sign in failed", e)

            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("GOOGLE", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SIGN IN", "signInWithCredential:success")
                    toMainActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("GOOGLE", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
    //-------------------------------------------


    private fun toMainActivity() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }
}
