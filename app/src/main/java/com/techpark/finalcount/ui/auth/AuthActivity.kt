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
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.*
import com.techpark.finalcount.MainActivity
import com.techpark.finalcount.R
import com.techpark.finalcount.databinding.ActivityAuthBinding
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.utils.VKUtils


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


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("OAR", "onActivityResult")
        if (requestCode == RC_SIGN_IN) { //--------GOOGLE---------
            handleGoogleLogin(data)
        } else { // --------------VK-------------
            Log.d("VK", "onActivityResult")
            handleVKLogin(requestCode, resultCode, data)
        }//----------------------------
    }

    // ----------------- GOOGLE -----------------
    fun loginWithGoogle(view: View){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
    }

    private fun handleGoogleLogin(data: Intent?){
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
            Log.w("GOOGLE", "Google sign in failed", e)
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("GOOGLE", "firebaseAuthWithGoogle:  ${acct.id!!} ${acct.idToken}")

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

    //------------------ GITHUB -----------------
    fun loginWithGithub(view: View) {
        val provider = OAuthProvider.newBuilder("github.com")
        val scopes = arrayListOf("user:email")
        provider.setScopes(scopes)
        val pendingResultTask = mAuth.pendingAuthResult
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener(
                    OnSuccessListener {
                        toMainActivity()
                    })
                .addOnFailureListener {
                    // Handle failure.
                }
        } else {
            mAuth
                .startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener {
                    toMainActivity()
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                    mAuth.signOut()
                    startActivity(Intent(applicationContext, AuthActivity::class.java))
                    Log.e("GITHUB", it.message!!)
                }
        }
    }
    //-------------------------------------------

    //-------------------- VK -------------------
    fun loginWithVK(view: View){
        VK.login(this, arrayListOf(VKScope.EMAIL))
        val fingerprints =
            VKUtils.getCertificateFingerprint(this, this.packageName)
        Log.d("VK", fingerprints!![0].toString())
        Log.d("VK", "onClick")

    }

    private fun handleVKLogin(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                if (token.email != null) {
                    token.accessToken.let {
                        mAuth.signInWithCustomToken(it)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("VK", "success")
                                    toMainActivity()
                                } else {
                                    Log.w("VK", it, task.exception)
                                    Toast.makeText(
                                        applicationContext,
                                        task.exception.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(applicationContext, "Email is empty. Please try with another account", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onLoginFailed(errorCode: Int) {
                Log.e("VK", "Login failed $errorCode")
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    //-------------------------------------------


    private fun toMainActivity() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }
}
