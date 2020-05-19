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
import com.techpark.finalcount.data.AndroidResourceManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthPresenterImpl @Inject constructor(val mResourceManager: AndroidResourceManager) : AuthPresenter, BasePresenterImpl<AuthView>() {
	private val mAuth = FirebaseAuth.getInstance()
	private var mError : String? = null

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
					showError(mError)
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
			val e: AuthResult? = withContext(mIOScope.coroutineContext) {
				authGoogle(account!!)
			}
			if (e != null) {
				Log.d("SIGN IN", "signInWithCredential:success")
				mView?.loginSuccess()
			} else {
				mView?.setLoadingVisibility(false)
				mView?.loginError()
				showError(mError)
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
			mError = e.localizedMessage
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
			return if (pendingResultTask != null) {
				withContext(mIOScope.coroutineContext) {
					pendingResultTask.await()
				}
			} else {
				withContext(mIOScope.coroutineContext) {
					mAuth
						.startActivityForSignInWithProvider(activity, provider.build())
						.await()
				}
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
			mError = e.localizedMessage
			null
		}
	}

	override fun authFacebook(token: AccessToken) {
		Log.d("FACEBOOK", "handleFacebookAccessToken:$token")
		mMainScope.launch {
			val res : AuthResult? = withContext(mIOScope.coroutineContext) {
				handleFacebook(token)
			}
			if (res != null) {
				Log.d("FACEBOOK", "signInWithCredential:success")
				mView?.loginSuccess()
			} else {
				mAuth.signOut()
				mView?.loginError()
				showError(mError)
			}
		}
	}

	private fun showError(err: String?) {
		err?.let {
			if (err == EMPTY_ERROR) {
				mView?.showError(mResourceManager.getStringInvalidError())
			} else {
				mView?.showError(it)
//				mView?.showError(mResourceProvider.getStrings().getInvalid())
			}
			mError = null
		} ?: run {
			mView?.showError(STANDARD_ERROR_MSG)
		}
	}

	private fun check(login: String, password: String): Boolean =
		login.isNotEmpty() && password.isNotEmpty()

	private suspend fun loginEmail(login: String, password: String): AuthResult? {
		Log.d("PRESENTER", "login")
		return withContext(mIOScope.coroutineContext) {
			try {
				mAuth
					.signInWithEmailAndPassword(login, password)
					.await()
			} catch (e: Exception) {
				mError = e.localizedMessage
				null
			}
		}
	}

	private suspend fun registerEmail(login: String, password: String): AuthResult? {
		return withContext(mIOScope.coroutineContext) {
			try {
				mAuth.createUserWithEmailAndPassword(login, password)
					.await()
			} catch (e: Exception) {
				mError = e.localizedMessage
				null
			}
		}
	}

	override fun checkLogin() {
		if (mAuth.currentUser != null) {
			mView?.isLogin(true)
		} else {
			mView?.isLogin(false)
		}
	}

	companion object {
		const val STANDARD_ERROR_MSG = "Some error occurred"
		const val EMPTY_ERROR = "Invalid login or password"
	}
}
