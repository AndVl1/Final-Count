package com.techpark.finalcount.auth.views.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.techpark.finalcount.R
import com.techpark.finalcount.auth.presenters.AuthPresenterImpl
import com.techpark.finalcount.auth.views.AuthView
import com.techpark.finalcount.base.BaseActivity
import com.techpark.finalcount.databinding.ActivityAuthBinding
import com.techpark.finalcount.main.views.activity.MainActivity
import com.techpark.finalcount.utils.Utils
import dagger.android.AndroidInjection
import javax.inject.Inject

/**
 *
 * Have to copy all new
 * code to AuthActivityTesting
 * until I have no idea how
 * to test successful auth cases
 *
 * */

class AuthActivity : BaseActivity(), AuthView {

	private lateinit var mLoginActivityBinding : ActivityAuthBinding
	private lateinit var mGoogleSignInClient: GoogleSignInClient
	private val mCallbackManager: CallbackManager = CallbackManager.Factory.create() // facebook

	@Inject
	lateinit var mAuthPresenter : AuthPresenterImpl

	override fun onCreate(savedInstanceState: Bundle?) {
		AndroidInjection.inject(this)
		super.onCreate(savedInstanceState)
		mLoginActivityBinding = ActivityAuthBinding.inflate(layoutInflater)

		setContentView(mLoginActivityBinding.root)


		mAuthPresenter.attachView(this)
		mAuthPresenter.checkLogin()

		mLoginActivityBinding.statusView.visibility = View.GONE


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

			mAuthPresenter.authenticateEmail(
				mLoginActivityBinding.loginInput.text.toString(),
				mLoginActivityBinding.passwordInput.text.toString(),
				mLoginActivityBinding.authTypeSwitch.isChecked
			)

		}

		mLoginActivityBinding.appNameTextView.typeface = Typeface.createFromAsset(assets, "fonts/indigo_daisy.ttf")

		//FACEBOOK
		mLoginActivityBinding.facebookLogin.setPermissions(listOf("email"))
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
		mLoginActivityBinding.facebookFrame.setOnClickListener {
			mLoginActivityBinding.facebookLogin.performClick()
		}
		//------------------
	}


	override fun showError(err: String) {
		mLoginActivityBinding.statusView.text = err
		mLoginActivityBinding.statusView.visibility = View.VISIBLE
	}

	override fun loginSuccess() {
		mLoginActivityBinding.statusView.text = getString(R.string.success) // For testing
		mLoginActivityBinding.statusView.visibility = View.VISIBLE // For testing
		toMainActivity()
	}

	override fun loginError() {
		setLoadingVisibility(false)
		Utils.showMessage(applicationContext, getString(R.string.login_error))
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

	//-------------------------------------------

	//------------------ GITHUB -----------------
	fun loginWithGithub(view: View) {
		mAuthPresenter.authGithub(this@AuthActivity)
	}
	//-------------------------------------------

	override fun setLoadingVisibility(vis: Boolean){
		mLoginActivityBinding.progressBar.visibility = if (vis) View.VISIBLE else View.GONE
	}

	private fun toMainActivity() {
		mLoginActivityBinding.progressBar.visibility = View.GONE
//        startActivity(Intent(applicationContext, AddingActivity::class.java))
		startActivity(Intent(applicationContext, MainActivity::class.java))
		finish()
	}

	override fun onDestroy() {
		super.onDestroy()
		mAuthPresenter.detachView()
	}

	companion object {
		private const val RC_SIGN_IN = 9001
	}
}
