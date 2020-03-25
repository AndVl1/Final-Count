package com.techpark.finalcount.auth.views.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.techpark.finalcount.R
import com.techpark.finalcount.auth.presenters.AuthPresenterImpl
import com.techpark.finalcount.auth.views.AuthView
import com.techpark.finalcount.base.BaseActivity
import com.techpark.finalcount.databinding.ActivityAuthBinding
import com.techpark.finalcount.main.MainActivity
import com.techpark.finalcount.utils.Utils


class AuthActivity : BaseActivity(), AuthView {

    private lateinit var mLoginActivityBinding : ActivityAuthBinding
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val mCallbackManager: CallbackManager = CallbackManager.Factory.create() // facebook
    private val mAuthPresenter: AuthPresenterImpl = AuthPresenterImpl(mAuth)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(mAuth.currentUser != null) {
            toMainActivity()
            return
        }
        mLoginActivityBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(mLoginActivityBinding.root)

        mAuthPresenter.attachView(this)
        mAuthPresenter.checkLogin()


        mLoginActivityBinding.loginInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mLoginActivityBinding.statusView.visibility = View.GONE
                mLoginActivityBinding.submitButton.isEnabled = s.toString().trim{ it <= ' '}.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })
        mLoginActivityBinding.authTypeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mLoginActivityBinding.submitButton.setText(R.string.login)
            } else {
                mLoginActivityBinding.submitButton.setText(R.string.register)
            }
        }
        mLoginActivityBinding.submitButton.setOnClickListener {
            Log.d("AUTH", "button click")
            mAuthPresenter.authenticateEmail(mLoginActivityBinding.loginInput.text.toString(),
                mLoginActivityBinding.passwordInput.text.toString(),
                mLoginActivityBinding.authTypeSwitch.isChecked)
        }

        //FACEBOOK
        mLoginActivityBinding.facebookLogin.setReadPermissions(listOf("email"))
        Log.d("FACEBOOK", "permissions set")
        LoginManager.getInstance().registerCallback(mCallbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("FACEBOOK", "facebook:onSuccess:$loginResult")
                mAuthPresenter.authFacebook(loginResult.accessToken)
                mLoginActivityBinding.progressBar.visibility = View.VISIBLE
            }

            override fun onCancel() {
                Log.d("FACEBOOK", "facebook:onCancel")
                startActivity(Intent(applicationContext, AuthActivity::class.java))
            }

            override fun onError(error: FacebookException) {
                Log.d("FACEBOOK", "facebook:onError", error)
                startActivity(Intent(applicationContext, AuthActivity::class.java))
            }
        })
        Log.d("FACEBOOK", "onCreate final")
        //------------------
    }


    override fun showError(err: String) {
        mLoginActivityBinding.statusView.text = err
        mLoginActivityBinding.statusView.visibility = View.VISIBLE
    }

    override fun loginSuccess() {
        Utils.showMessage(this, "Login success!")
        toMainActivity()
    }

    override fun loginError() {
        setLoadingVisibility(false)
        Utils.showMessage(this, "Login error!")
    }

    override fun isLogin(isLogin: Boolean) {
        if (isLogin) {
            toMainActivity()
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("OAR", "onActivityResult")
        if (requestCode == RC_SIGN_IN) { //--------GOOGLE---------
            mAuthPresenter.handleGoogleAuth(data)
        } else {
            Log.d("OAR", "not google")
            mCallbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    // ----------------- GOOGLE -----------------
    fun loginWithGoogle(view: View){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        startActivityForResult(mGoogleSignInClient.signInIntent, RC_SIGN_IN)
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
        setLoadingVisibility(true)
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener(
                    OnSuccessListener {
                        toMainActivity()
                    })
                .addOnFailureListener {
                    mAuth.signOut()
                    startActivity(Intent(applicationContext, AuthActivity::class.java))
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
                    setLoadingVisibility(false)
                    Log.e("GITHUB", it.message ?: "Smth with Github")
                }
        }
    }
    //-------------------------------------------

    override fun setLoadingVisibility(vis: Boolean){
        mLoginActivityBinding.progressBar.visibility = if (vis) View.VISIBLE else View.GONE
    }

    private fun toMainActivity() {
        mLoginActivityBinding.progressBar.visibility = View.GONE
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAuthPresenter.detachView()
    }
}
