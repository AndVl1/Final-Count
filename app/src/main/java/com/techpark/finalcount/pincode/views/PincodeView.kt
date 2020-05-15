package com.techpark.finalcount.pincode.views

import com.techpark.finalcount.base.BaseView

interface PincodeView: BaseView {
	fun pinSuccess(login: Boolean)
	fun pinFailed()
	fun addInput(position: Int)
	fun clear(toApprove: Boolean)
	fun showMessage(msg: String)
	fun setFingerprintVisible(flag: Boolean)
	fun displayBiometricPrompt(login: Boolean)
}