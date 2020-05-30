package com.techpark.finalcount.fcm.presenter

import android.app.Service
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.RemoteMessage
import com.techpark.finalcount.auth.presenters.AuthPresenterImpl
import com.techpark.finalcount.data.DataSource
import com.techpark.finalcount.data.room.GlobalPreferences
import com.techpark.finalcount.data.room.model.Purchase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FcmPresenterImpl @Inject constructor(private val mPreferences: GlobalPreferences, dataSource: DataSource)
	: FcmPresenter {
	private val mMainScope = CoroutineScope(Dispatchers.IO)
	private val mIOScope = CoroutineScope(Dispatchers.Main)
	private val mAuth = FirebaseAuth.getInstance()
	private val mStorage = FirebaseFirestore.getInstance()
	private val mPurchaseDao = dataSource.purchaseDatabase.purchaseDao()
	private var mService: Service? = null

	override fun saveRegistrationToken(token: String) {
		mPreferences.putToken(token)
	}

	override fun handleMessage(remoteMessage: RemoteMessage) {
		val title = remoteMessage.notification?.title
		val body = remoteMessage.notification?.body
		Log.d(TAG, "$title, $body")
		if (title == "PURCHASE") {
			handleNewPurchase(body!!)
		}
	}

	override fun attachService(service: Service) {
		mService = service
	}

	override fun detachService() {
		mService = null
	}

	private fun handleNewPurchase(date: String) {
		mMainScope.launch {
			val dateLong = date.toLong()
			val purchases = withContext(mIOScope.coroutineContext) {
				mPurchaseDao.getByDate(dateLong)
			}
			if (purchases == null) {
				mIOScope.launch {
					val fromFB = mStorage.collection("purchases")
						.whereEqualTo("date", dateLong)
						.get()
						.await()
					Log.d(TAG, fromFB.size().toString())
					for (elem in fromFB) {
						Log.d(AuthPresenterImpl.TAG, elem["purchase"].toString())
						val hashMap = elem["purchase"] as HashMap<*, *>
						val purchase = Purchase(
							hashMap["id"] as Long,
							hashMap["name"] as String,
							(hashMap["cost"] as Long).toInt(),
							hashMap["date"] as Long
						)
						mIOScope.launch {
							mPurchaseDao.insert(purchase)
						}.join()
					}
				}
			}
		}
	}
	companion object {
		const val TAG = "FCM presenter"
	}
}