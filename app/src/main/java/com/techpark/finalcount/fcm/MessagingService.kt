package com.techpark.finalcount.fcm

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.techpark.finalcount.R
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

	fun showDeletableNotification(title: String) {
		val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			?: return
		val builder = NotificationCompat.Builder(this, CHANNEL_UPDATE)

		builder.setContentTitle(title)
			.setSmallIcon(R.drawable.circle_entered)
			.setContentText("Updating") // todo move to strings
			.setAutoCancel(true)

		manager.notify(DELETABLE_ID, builder.build())
		manager.cancel(DELETABLE_ID)

	}

	private fun saveRegistrationToken(token: String) {
		mFcmPresenter.saveRegistrationToken(token)
	}

	companion object {
		const val TAG = "FCM"
		const val CHANNEL_UPDATE = "Updating"
		const val DELETABLE_ID = 159
	}
}