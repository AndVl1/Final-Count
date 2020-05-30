package com.techpark.finalcount.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.techpark.finalcount.fcm.presenter.FcmPresenterImpl
import dagger.android.AndroidInjection
import javax.inject.Inject

class MessagingService : FirebaseMessagingService() {

	@Inject
	lateinit var mFcmPresenter: FcmPresenterImpl

	override fun onCreate() {
		AndroidInjection.inject(this)
		super.onCreate()
		mFcmPresenter.attachService(this)
		Log.d(TAG, "FCM onCreate")
	}

	override fun onDestroy() {
		super.onDestroy()
		mFcmPresenter.detachService()
	}

	override fun onNewToken(token: String) {
		super.onNewToken(token)
		saveRegistrationToken(token)
		Log.d(TAG, token)
	}

	override fun onMessageReceived(remoteMessage: RemoteMessage) {
		mFcmPresenter.handleMessage(remoteMessage)
	}

	private fun saveRegistrationToken(token: String) {
		mFcmPresenter.saveRegistrationToken(token)
	}

	companion object {
		const val TAG = "FCM"
	}
}