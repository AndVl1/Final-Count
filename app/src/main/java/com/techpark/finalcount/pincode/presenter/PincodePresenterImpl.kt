package com.techpark.finalcount.pincode.presenter

import android.util.Log
import com.techpark.finalcount.base.BasePresenterImpl
import com.techpark.finalcount.data.PinPreferences
import com.techpark.finalcount.pincode.BiometricUtils
import com.techpark.finalcount.pincode.views.PincodeView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PincodePresenterImpl(private val mPrefs: PinPreferences): PincodePresenter, BasePresenterImpl<PincodeView>() {
	var mCurrentInput = StringBuilder()
	override fun addNumber(num: String) {
		Log.d(TAG, "$num $mCurrentInput")
		if (mCurrentInput.length < 4) {
			mCurrentInput.append(num)
			mView?.addInput(mCurrentInput.length)
		}
		if (mCurrentInput.length == 4) {
			check()
		}

	}

	private fun check() {
		MainScope().launch {
			delay(500)
			if (mPrefs.checkPin(mCurrentInput.toString())) {
				mCurrentInput.clear()
				mView?.pinSuccess(true)
			} else {
				mCurrentInput.clear()
				mView?.pinFailed()
			}
		}
	}

	override fun clear() {
		mCurrentInput.clear()
		mView?.clear(false)
	}

	override fun handleScanner(success: Boolean) {
		if (success) {
			mView?.pinSuccess(true)
		}
	}

	override fun attachView(view: PincodeView) {
		super.attachView(view)
		if (BiometricUtils.isSdkVersionSupported() &&
			BiometricUtils.isBiometricPromptEnabled() &&
			mPrefs.hasScanner()) {
			mView?.setFingerprintVisible(true)
			mView?.displayBiometricPrompt(true)
		} else {
			mView?.setFingerprintVisible(false)
		}
	}


	companion object {
		const val TAG = "PIN PRESENTER"
	}
}