package com.techpark.finalcount.fcm.presenter

import com.google.firebase.messaging.RemoteMessage
import com.techpark.finalcount.fcm.MessagingService

interface FcmPresenter {
	fun saveRegistrationToken(token: String)
	fun handleMessage(remoteMessage: RemoteMessage)
	fun attachService(service: MessagingService)
	fun detachService()
}