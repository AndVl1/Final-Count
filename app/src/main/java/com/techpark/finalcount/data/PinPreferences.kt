package com.techpark.finalcount.data

import android.content.Context
import android.content.SharedPreferences
import com.securepreferences.SecurePreferences

class PinPreferences (context: Context){
	private val mSecurePrefs = SecurePreferences(context, "1234", PIN_PREFS)

	fun checkPin(string: String): Boolean {
		return mSecurePrefs.getString(PIN_NAME, "0000")!! == string
	}

	fun setPin(pin: String) {
		edit().putString(PIN_NAME, pin)
			.putBoolean(HAS_PIN, true)
			.apply()
	}

	fun addScanner() {
		edit().putBoolean(HAS_SCANNER, true).apply()
	}

	fun removePin() {
		edit().putBoolean(HAS_PIN, false)
			.putBoolean(HAS_SCANNER, false)
			.apply()
	}

	fun hasScanner(): Boolean = mSecurePrefs.getBoolean(HAS_SCANNER, false)

	fun hasPin(): Boolean = mSecurePrefs.getBoolean(HAS_PIN, false)

	private fun edit(): SharedPreferences.Editor = mSecurePrefs.edit()

	companion object {
		const val PIN_PREFS = "FinCountPrefsPincode"
		const val PIN_NAME = "Pin"
		const val HAS_PIN = "HAS_PIN"
		const val HAS_SCANNER = "HAS_SCANNER"
	}
}