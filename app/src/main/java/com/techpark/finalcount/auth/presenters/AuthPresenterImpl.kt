package com.techpark.finalcount.auth.presenters

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.techpark.finalcount.auth.views.AuthView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthPresenterImpl : AuthPresenter {
    private val mAuth = FirebaseAuth.getInstance()
    private var mAuthView: AuthView? = null
    private val job = Job()
    private val mScope = CoroutineScope(job + Dispatchers.Main)

    override fun authenticateEmail(login: String, password: String, isLogin: Boolean) {
        Log.d("PRESENTER", "auth")
        mAuthView?.setLoadingVisibility(true)
        if (!check(login, password)) {
            mAuthView?.showError("Invalid login or password")
            mAuthView?.loginError()
        } else {
            mScope.launch {
                val res: AuthResult? = if (isLogin) {
                    loginEmail(login, password)
                } else {
                    registerEmail(login, password)
                }
                if (res != null) {
                    mAuthView?.loginSuccess()
                } else {
                    mAuthView?.loginError()
                }
            }
        }
    }

    override fun handleGoogleAuth(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        // Google Sign In was successful, authenticate with Firebase
        val account = task.getResult(ApiException::class.java)
        mAuthView?.setLoadingVisibility(true)
        mScope.launch {
            val e = authGoogle(account!!)
            if (e != null) {
                Log.d("SIGN IN", "signInWithCredential:success")
                mAuthView?.loginSuccess()
            } else {
                mAuthView?.setLoadingVisibility(false)
                mAuthView?.loginError()
            }
        }
    }

    private suspend fun authGoogle(acct: GoogleSignInAccount): AuthResult? {
        Log.d("GOOGLE", "firebaseAuthWithGoogle:  ${acct.id} ${acct.idToken}")
        return try {
            val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
            mAuth.signInWithCredential(credential)
                .await()
        } catch (e: Exception) {
            mAuthView?.showError(e.localizedMessage ?: "Some error occurred")
            Log.w("GOOGLE", "Google sign in failed", e)
            null
        }
    }

    override fun authGithub(activity: Activity) {
        mScope.launch {
            val result = handleGithub(activity)
            if (result != null) {
                mAuthView?.loginSuccess()
            } else {
                mAuth.signOut()
                mAuthView?.loginError()
                mAuthView?.setLoadingVisibility(false)
            }
        }
    }

    private suspend fun handleGithub(activity: Activity): AuthResult? {
        val provider = OAuthProvider.newBuilder("github.com")
        val scopes = arrayListOf("user:email")
        provider.setScopes(scopes)
        val pendingResultTask = mAuth.pendingAuthResult
        mAuthView?.setLoadingVisibility(true)
        // There's something already here! Finish the sign-in for your user.
        return try {
            if (pendingResultTask != null) {
                pendingResultTask
                    .await()
            } else {
                mAuth
                    .startActivityForSignInWithProvider(activity, provider.build())
                    .await()
            }
        } catch (e: Exception) {
            mAuthView?.showError(e.localizedMessage ?: "Some error occurred")
            Log.e("GITHUB", "Sign in failed", e)
            null
        }
    }

    private suspend fun handleFacebook(token: AccessToken): AuthResult? {
        val credential = FacebookAuthProvider.getCredential(token.token)
        return try {
            mAuth.signInWithCredential(credential)
                .await()
        } catch (e: Exception) {
            Log.w("FACEBOOK", "signInWithCredential:failure", e)
            mAuthView?.showError("Auth failed")
            null
        }
    }

    override fun authFacebook(token: AccessToken) {
        Log.d("FACEBOOK", "handleFacebookAccessToken:$token")
        mScope.launch {
            val res = handleFacebook(token)
            if (res != null) {
                Log.d("FACEBOOK", "signInWithCredential:success")
                mAuthView?.loginSuccess()

            } else {
                mAuth.signOut()
                mAuthView?.loginError()
            }
        }
    }

    private fun check(login: String, password: String): Boolean =
        login.isNotEmpty() && password.isNotEmpty()

    private suspend fun loginEmail(login: String, password: String): AuthResult? {
        Log.d("PRESENTER", "login")
        return try {
            mAuth
                .signInWithEmailAndPassword(login, password)
                .await()
        } catch (e: Exception) {
            mAuthView?.showError(e.localizedMessage ?: "Some error occurred")
            null
        }
    }

    private suspend fun registerEmail(login: String, password: String): AuthResult? {
        return try {
            mAuth.createUserWithEmailAndPassword(login, password)
                .await()
        } catch (e: Exception) {
            mAuthView?.showError(e.localizedMessage ?: "Some error occurred")
            null
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
        } else {
            mAuthView?.isLogin(false)
        }
    }
}
