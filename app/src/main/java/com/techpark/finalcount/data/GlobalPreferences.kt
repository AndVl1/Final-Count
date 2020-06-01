package com.techpark.finalcount.data

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class GlobalPreferences @Inject constructor(context: Context) {
	private val mPreferences = context.getSharedPreferences("FinalCount", Context.MODE_PRIVATE)

	fun getFcmToken() : String? = mPreferences.getString(FCM, "")

	fun putToken(token: String) {
		editPrefs()
			.putString(FCM, token)
			.apply()
	}

	fun deleteToken() {
		editPrefs().remove(FCM).apply()
	}

	private fun editPrefs(): SharedPreferences.Editor = mPreferences.edit()

	companion object {
		const val FCM = "FCM_TOKEN"
	}
}