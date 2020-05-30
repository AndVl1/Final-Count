package com.techpark.finalcount.fcm.presenter

import android.app.Service
import com.google.firebase.messaging.RemoteMessage

interface FcmPresenter {
	fun saveRegistrationToken(token: String)
	fun handleMessage(remoteMessage: RemoteMessage)
	fun attachService(service: Service)
	fun detachService()
}