package com.techpark.finalcount.main.presenters

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.techpark.finalcount.base.BasePresenterImpl
import com.techpark.finalcount.data.DataSource
import com.techpark.finalcount.data.dbexpimp.JsonDbExportImportApiKt
import com.techpark.finalcount.data.room.GlobalPreferences
import com.techpark.finalcount.data.room.model.Planning
import com.techpark.finalcount.main.views.MainView
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class MainPresenterImpl @Inject constructor(private val dataSource: DataSource, private val mPrefs: GlobalPreferences)
	: MainPresenter, BasePresenterImpl<MainView>() {
	private val mPurchaseDao = dataSource.purchaseDatabase.purchaseDao()
	private val mPlanningDao = dataSource.planningDatabase.planningDao()
	override fun saveAll(root: String) {
		mMainScope.launch {
			var file: File?
			val jsonArray = JsonDbExportImportApiKt.exportPurchaseDbToJsonArray(dataSource.purchaseDatabase.purchaseDao())
			if (jsonArray != null) {
				file = withContext(mIOScope.coroutineContext) {
					JsonDbExportImportApiKt.saveCsv(root, jsonArray)
				}
				Log.d(TAG, jsonArray.toString())
				if (file.exists()) {
					mView?.startShareIntent(file)
				}
			} else {
				mView?.showMsg("error occurred while saving")
			}
		}
	}

	override fun logOut() {
		mMainScope.launch {
			val auth = FirebaseAuth.getInstance()

			mIOScope.launch {
				mPlanningDao.clear()
				mPurchaseDao.clear()
				FirebaseMessaging.getInstance().unsubscribeFromTopic(auth.currentUser!!.uid)
			}.join()
			auth.signOut()
			mView?.toAuthActivity()
		}
	}

	override fun clear() {
		mIOScope.launch {
			mPurchaseDao.clear()
			for (elem in mPlanningDao.loadAll()) {
				mPlanningDao.update(Planning
					(elem.id, elem.begin, elem.end, elem.planned, 0)
				)
			}
			val list = FirebaseFirestore.getInstance()
				.collection("purchases")
				.whereEqualTo("uid", FirebaseAuth.getInstance().currentUser!!.uid)
				.get()
				.await()
			for (elem in list) {
				elem.reference.delete()
			}
		}
	}

	companion object {
		const val TAG = "MAIN PRESENTER"
	}
}