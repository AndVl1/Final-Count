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
import com.techpark.finalcount.base.BasePresenterImpl
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthPresenterImpl : AuthPresenter, BasePresenterImpl<AuthView>() {
	private val mAuth = FirebaseAuth.getInstance()

	override fun authenticateEmail(login: String, password: String, isLogin: Boolean) {
		Log.d("PRESENTER", "auth")
		mView?.setLoadingVisibility(true)
		if (!check(login, password)) {
			mView?.showError("Invalid login or password")
			mView?.loginError()
		} else {
			mMainScope.launch {
				val res: AuthResult? = if (isLogin) {
					loginEmail(login, password)
				} else {
					registerEmail(login, password)
				}
				if (res != null) {
					mView?.loginSuccess()
				} else {
					mView?.loginError()
				}
			}
		}
	}

	override fun handleGoogleAuth(data: Intent?) {
		val task = GoogleSignIn.getSignedInAccountFromIntent(data)
		// Google Sign In was successful, authenticate with Firebase
		val account = task.getResult(ApiException::class.java)
		mView?.setLoadingVisibility(true)
		mMainScope.launch {
			var e: AuthResult? = null
			mIOScope.launch {
				e = authGoogle(account!!)
			}.join()
			if (e != null) {
				Log.d("SIGN IN", "signInWithCredential:success")
				mView?.loginSuccess()
			} else {
				mView?.setLoadingVisibility(false)
				mView?.loginError()
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
			mView?.showError(e.localizedMessage ?: "Some error occurred")
			Log.w("GOOGLE", "Google sign in failed", e)
			null
		}
	}

	override fun authGithub(activity: Activity) {
		mMainScope.launch {
			val result = handleGithub(activity)
			if (result != null) {
				mView?.loginSuccess()
			} else {
				mAuth.signOut()
				mView?.loginError()
				mView?.setLoadingVisibility(false)
			}
		}
	}

	private suspend fun handleGithub(activity: Activity): AuthResult? {
		val provider = OAuthProvider.newBuilder("github.com")
		val scopes = arrayListOf("user:email")
		provider.setScopes(scopes)
		val pendingResultTask = mAuth.pendingAuthResult
		mView?.setLoadingVisibility(true)
		// There's something already here! Finish the sign-in for your user.
		try {
			if (pendingResultTask != null) {
				var result: AuthResult? = null
				mIOScope.launch {
					result = pendingResultTask
						.await()
				}.join()
				return result
			} else {
				var result: AuthResult? = null
				mIOScope.launch {
					result = mAuth
						.startActivityForSignInWithProvider(activity, provider.build())
						.await()
				}.join()
				return result
			}
		} catch (e: Exception) {
			mView?.showError(e.localizedMessage ?: "Some error occurred")
			Log.e("GITHUB", "Sign in failed", e)
			return null
		}
	}

	private suspend fun handleFacebook(token: AccessToken): AuthResult? {
		val credential = FacebookAuthProvider.getCredential(token.token)
		return try {
			mAuth.signInWithCredential(credential)
				.await()
		} catch (e: Exception) {
			Log.w("FACEBOOK", "signInWithCredential:failure", e)
			mView?.showError("Auth failed")
			null
		}
	}

	override fun authFacebook(token: AccessToken) {
		Log.d("FACEBOOK", "handleFacebookAccessToken:$token")
		mMainScope.launch {
			var res : AuthResult? = null
			mIOScope.launch {
				res = handleFacebook(token)
			}.join()
			if (res != null) {
				Log.d("FACEBOOK", "signInWithCredential:success")
				mView?.loginSuccess()

			} else {
				mAuth.signOut()
				mView?.loginError()
			}
		}
	}

	private fun check(login: String, password: String): Boolean =
		login.isNotEmpty() && password.isNotEmpty()

	private suspend fun loginEmail(login: String, password: String): AuthResult? {
		Log.d("PRESENTER", "login")
		var result : AuthResult? = null
		mIOScope.launch {
			result = try {
				mAuth
					.signInWithEmailAndPassword(login, password)
					.await()
			} catch (e: Exception) {
				mView?.showError(e.localizedMessage ?: "Some error occurred")
				null
			}
		}.join()
		return result
	}

	private suspend fun registerEmail(login: String, password: String): AuthResult? {
		return try {
			mAuth.createUserWithEmailAndPassword(login, password)
				.await()
		} catch (e: Exception) {
			mView?.showError(e.localizedMessage ?: "Some error occurred")
			null
		}
	}

	override fun checkLogin() {
		if (mAuth.currentUser != null) {
			mView?.isLogin(true)
		} else {
			mView?.isLogin(false)
		}
	}
}
