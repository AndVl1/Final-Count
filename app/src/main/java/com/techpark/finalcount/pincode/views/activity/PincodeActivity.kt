package com.techpark.finalcount.pincode.views.activity

import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import com.techpark.finalcount.R
import com.techpark.finalcount.base.BaseActivity
import com.techpark.finalcount.data.PinPreferences
import com.techpark.finalcount.databinding.ActivityPincodeBinding
import com.techpark.finalcount.main.views.activity.MainActivity
import com.techpark.finalcount.pincode.BiometricUtils
import com.techpark.finalcount.pincode.presenter.PincodeAddingPresenterImpl
import com.techpark.finalcount.pincode.presenter.PincodePresenter
import com.techpark.finalcount.pincode.presenter.PincodePresenterImpl
import com.techpark.finalcount.pincode.views.PincodeView
import java.util.concurrent.Executor


class PincodeActivity : BaseActivity(), PincodeView {
	private lateinit var mPincodeBinding: ActivityPincodeBinding
	private lateinit var mPincodePresenter: PincodePresenter
	private val circleBorderGreen = R.drawable.circle_border_green
	private val circleBorderRed = R.drawable.circle_border_red
	private val circleEntered = R.drawable.circle_entered

	/** Biometric */
	private lateinit var executor: Executor
	private lateinit var biometricPrompt: BiometricPrompt
	private lateinit var promptInfo: BiometricPrompt.PromptInfo

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mPincodeBinding = ActivityPincodeBinding.inflate(layoutInflater)
		setContentView(mPincodeBinding.root)

		supportActionBar?.hide()
//        mSharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE)

		val intent = intent
		mPincodePresenter = if (intent.getBooleanExtra("login", true)) {
			PincodePresenterImpl(PinPreferences(this))
		} else
			PincodeAddingPresenterImpl(PinPreferences(this))

		mPincodePresenter.attachView(this)

		/** Click listeners*/
		KeyboardHandler()
	}


	override fun onDestroy() {
		super.onDestroy()
		mPincodePresenter.detachView()
	}

	override fun setFingerprintVisible(flag: Boolean) {
		if (!flag) {
			mPincodeBinding.pinFinger.visibility = View.INVISIBLE
		} else {
			mPincodeBinding.pinFinger.visibility = View.VISIBLE
		}
	}

	override fun addInput(position: Int) {
		Log.d(TAG, "add $position")
		when (position) {
			CIRCLE_FIRST -> imageViewAnimatedChange(mPincodeBinding.imageviewCircle1, circleEntered)
			CIRCLE_SECOND -> imageViewAnimatedChange(mPincodeBinding.imageviewCircle2, circleEntered)
			CIRCLE_THIRD -> imageViewAnimatedChange(mPincodeBinding.imageviewCircle3, circleEntered)
			CIRCLE_FOURTH -> imageViewAnimatedChange(mPincodeBinding.imageviewCircle4, circleEntered)
		}
	}

	private fun imageViewAnimatedChange(v: ImageView, res: Int) {
		val animOut: Animation =
			AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out)
		val animIn: Animation =
			AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_in)
		animOut.setAnimationListener(object : AnimationListener {
			override fun onAnimationStart(animation: Animation) {}
			override fun onAnimationRepeat(animation: Animation) {}
			override fun onAnimationEnd(animation: Animation) {
				v.setImageResource(res)
				animIn.setAnimationListener(object : AnimationListener {
					override fun onAnimationStart(animation: Animation) {}
					override fun onAnimationRepeat(animation: Animation) {}
					override fun onAnimationEnd(animation: Animation) {}
				})
				v.startAnimation(animIn)
			}
		})
		v.startAnimation(animOut)
	}

	override fun pinSuccess(login: Boolean) {
		if (login) {
			startActivity(Intent(applicationContext, MainActivity::class.java))
			finish()
			return
		} else {
			if (BiometricUtils.isBiometricPromptEnabled() &&
				BiometricUtils.isFingerprintAvailable(this)) {
				ScannerDialog().show(supportFragmentManager, "dialog")
			} else {
				onBackPressed()
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.P)
	override fun displayBiometricPrompt(login: Boolean) {

		executor = ContextCompat.getMainExecutor(this)

		biometricPrompt = BiometricPrompt(this, executor,
			object : BiometricPrompt.AuthenticationCallback() {

				override fun onAuthenticationSucceeded(
					result: BiometricPrompt.AuthenticationResult
				) {
					super.onAuthenticationSucceeded(result)
					if (login) {
						mPincodePresenter.handleScanner(true)
						Log.d(TAG, "biometric login")
					} else {
						Log.d(TAG, "biometric !login")
						mPincodePresenter.handleScanner(true)
						onBackPressed()
					}
				}

				override fun onAuthenticationFailed() {
					super.onAuthenticationFailed()
					Toast.makeText(
						applicationContext, "Authentication failed",
						Toast.LENGTH_SHORT
					).show()
				}
			})

		promptInfo = BiometricPrompt.PromptInfo.Builder()
			.setTitle(getString(R.string.use_biometric))
			.setSubtitle(
				if (login)
					getString(R.string.use_biometric_login)
				else
					getString(R.string.use_biometric_add)
			)
			.setNegativeButtonText(getString(R.string.cancel))
			.build()

		Log.d(TAG, "prompt")
		biometricPrompt.authenticate(promptInfo)
	}

	override fun showMessage(msg: String) {
		Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
	}

	override fun clear(toApprove: Boolean) {
		if (toApprove) {
			mPincodeBinding.pinMainText.text = getString(R.string.repeat_pin)
		}
		cancel(circleBorderGreen)
	}

	private fun cancel(mode: Int) {
		imageViewAnimatedChange(mPincodeBinding.imageviewCircle1, mode)
		imageViewAnimatedChange(mPincodeBinding.imageviewCircle2, mode)
		imageViewAnimatedChange(mPincodeBinding.imageviewCircle3, mode)
		imageViewAnimatedChange(mPincodeBinding.imageviewCircle4, mode)
	}

	override fun pinFailed() {
		mPincodeBinding.pinMainText.text = getString(R.string.enter_pin)
		cancel(circleBorderRed)
	}

	override fun onBackPressed() {
		super.onBackPressed()
		Log.d(TAG, "onBackPressed")
	}

	inner class KeyboardHandler {
		init {
			for (number in mPincodeBinding.numPad.children) {
				number.setOnClickListener {
					mPincodePresenter.addNumber((number as TextView).text.toString())
				}
			}

			mPincodeBinding.pinFinger.setOnClickListener {
				displayBiometricPrompt(true)
			}
			mPincodeBinding.pinCancel.setOnClickListener {
				mPincodePresenter.clear()
			}
		}
	}

	companion object {
		const val TAG = "PINCODE ACTIVITY"
		private const val CIRCLE_FIRST = 1
		private const val CIRCLE_SECOND = 2
		private const val CIRCLE_THIRD = 3
		private const val CIRCLE_FOURTH = 4
	}

	fun dialogYesClicked() {
		displayBiometricPrompt(false)
	}
}

class ScannerDialog : DialogFragment() {
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val builder = AlertDialog.Builder(activity)
		builder.setMessage(R.string.scanner_ask)
			.setPositiveButton(R.string.yes) { _, _ ->
				(activity as PincodeActivity).dialogYesClicked()
			}
			.setNegativeButton(R.string.no) { dialog, _ ->
				dialog.cancel()
				(activity as PincodeActivity).onBackPressed()
			}

		return builder.create()
	}
}
