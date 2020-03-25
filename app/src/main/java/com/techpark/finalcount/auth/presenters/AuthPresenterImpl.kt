package com.techpark.finalcount.auth.presenters

import android.content.Intent
import android.util.Log
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.techpark.finalcount.auth.views.AuthView

class AuthPresenterImpl(private val mAuth: FirebaseAuth): AuthPresenter {
    private var mAuthView: AuthView? = null
    override fun authenticateEmail(login: String, password: String, isLogin: Boolean) {
        Log.d("PRESENTER", "auth")
        mAuthView?.setLoadingVisibility(true)
        if (!check(login, password)){
            mAuthView?.showError("Invalid login or password")
        } else {
            if (isLogin){
                login(login, password)
            } else {
                register(login, password)
            }
        }
    }

    override fun handleGoogleAuth(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)
            mAuthView?.setLoadingVisibility(true)
            authGoogle(account!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            mAuthView?.setLoadingVisibility(false)
            mAuthView?.showError(e.localizedMessage ?: "Some error occurred")
            Log.w("GOOGLE", "Google sign in failed", e)
        }
    }

    override fun authGoogle(acct: GoogleSignInAccount) {
        Log.d("GOOGLE", "firebaseAuthWithGoogle:  ${acct.id} ${acct.idToken}")

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SIGN IN", "signInWithCredential:success")
                    mAuthView?.loginSuccess()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("GOOGLE", "signInWithCredential:failure", task.exception)
                    mAuthView?.loginError()
                    mAuthView?.showError(task.exception.toString())
                }
            }
    }

    override fun authGithub() {
//        val provider = OAuthProvider.newBuilder("github.com")
//        val scopes = arrayListOf("user:email")
//        provider.setScopes(scopes)
//        val pendingResultTask = mAuth.pendingAuthResult
//        mAuthView!!.setLoadingVisibility(true)
//        if (pendingResultTask != null) {
//            // There's something already here! Finish the sign-in for your user.
//            pendingResultTask
//                .addOnSuccessListener(
//                    OnSuccessListener {
//                        mAuthView!!.loginSuccess()
//                    })
//                .addOnFailureListener {
//                    mAuth.signOut()
//                    mAuthView!!.loginError()
//                }
//        } else {
//            mAuth
//                .startActivityForSignInWithProvider(this, provider.build())
//                .addOnSuccessListener {
//                    mAuthView!!.loginSuccess()
//                }
//                .addOnFailureListener {
//                    mAuthView!!.showError(it.localizedMessage!!)
//                    mAuth.signOut()
//                    mAuthView!!.loginError()
//                    Log.e("GITHUB", it.message!!)
//                }
//        }
    }

    override fun authFacebook(token: AccessToken) {
        Log.d("FACEBOOK", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("FACEBOOK", "signInWithCredential:success")
                    mAuthView?.loginSuccess()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("FACEBOOK", "signInWithCredential:failure", task.exception)
                    mAuthView?.showError("Auth failed")
                    mAuth.signOut()
                    mAuthView?.loginError()
                }
            }
    }

    private fun check(login: String, password: String):Boolean = login.isNotEmpty() && password.isNotEmpty()
    private fun login(login: String, password: String){
        Log.d("PRESENTER", "login")
        mAuth.signInWithEmailAndPassword(login, password)
            .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mAuthView?.loginSuccess()
                    } else {
                        mAuthView?.showError(task.exception?.localizedMessage ?: "Some error occurred")
                        mAuthView?.loginError()
                    }
            }
    }

    private fun register(login: String, password: String){
        mAuth.createUserWithEmailAndPassword(login, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mAuthView?.loginSuccess()
                } else {
                    mAuthView?.showError(task.exception?.localizedMessage ?: "Some error occurred")
                    mAuthView?.loginError()
                }
        }
    }

    override fun attachView(view: AuthView) {
        mAuthView = view
    }

    override fun detachView() {
        mAuthView = null
    }

    override fun checkLogin() {
        if (mAuth.currentUser != null) {
            mAuthView?.isLogin(true)
        }else{
            mAuthView?.isLogin(false)
        }
    }
}
