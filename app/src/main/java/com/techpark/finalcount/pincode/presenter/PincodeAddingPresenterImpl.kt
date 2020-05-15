package com.techpark.finalcount.pincode.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.techpark.finalcount.base.BasePresenterImpl
import com.techpark.finalcount.data.PinPreferences
import com.techpark.finalcount.pincode.views.PincodeView

class PincodeAddingPresenterImpl(var context: Context): PincodePresenter, BasePresenterImpl<PincodeView>() {
	private var mCurrentInput = StringBuilder()
	private var mFirstInput = StringBuilder()
	private val mPrefs = PinPreferences(context)

	override fun addNumber(num: String) {
		Log.d(PincodePresenterImpl.TAG, "$num $mCurrentInput")
		if (mCurrentInput.length < 4) {
			mCurrentInput.append(num)
			mView?.addInput(mCurrentInput.length)
		}
		if (mCurrentInput.length == 4) {
			if (mFirstInput.isEmpty()) {
				Log.d(TAG, mCurrentInput.toString())
				save()
			} else {
				check()
			}
		}
	}

	private fun check() {
		if (mCurrentInput.toString() == mFirstInput.toString()) {
			Log.d(TAG, "$mCurrentInput, $mFirstInput")
			mPrefs.setPin(mCurrentInput.toString())
			mView?.pinSuccess(false)
		} else {
			mCurrentInput.clear()
			mFirstInput.clear()
			mView?.pinFailed()
			mView?.showMessage("Try again")
		}
	}

	private fun save() {
		mFirstInput.append(mCurrentInput)
		mCurrentInput.clear()
		mView?.clear(true)
	}

	override fun clear() {
		mCurrentInput.clear()
		mView?.clear(false)
	}

	override fun handleScanner(success: Boolean) {
		if (success) {
			mPrefs.addScanner()

		}
	}

	override fun attachView(view: PincodeView) {
		super.attachView(view)
		mView?.setFingerprintVisible(false)
	}

	companion object {
		const val TAG = "PIN PRESENTER CREATE"
	}
}