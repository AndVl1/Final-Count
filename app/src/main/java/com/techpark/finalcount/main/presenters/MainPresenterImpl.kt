package com.techpark.finalcount.main.presenters

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.techpark.finalcount.base.BasePresenterImpl
import com.techpark.finalcount.data.DataSource
import com.techpark.finalcount.data.dbexpimp.JsonDbExportImportApiKt
import com.techpark.finalcount.data.room.GlobalPreferences
import com.techpark.finalcount.data.room.model.Planning
import com.techpark.finalcount.main.views.MainView
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainPresenterImpl @Inject constructor(private val dataSource: DataSource, private val mPrefs: GlobalPreferences)
	: MainPresenter, BasePresenterImpl<MainView>() {
	private val mPurchaseDao = dataSource.purchaseDatabase.purchaseDao()
	private val mPlanningDao = dataSource.planningDatabase.planningDao()
	override fun saveAll(root: String) {
		mMainScope.launch {
			val path: String
			val jsonArray = JsonDbExportImportApiKt.exportPurchaseDbToJsonArray(dataSource.purchaseDatabase.purchaseDao())
			if (jsonArray != null) {
				path = withContext(mIOScope.coroutineContext) {
					JsonDbExportImportApiKt.saveCsv(root, jsonArray)
				}
				Log.d(TAG, jsonArray.toString())
				mView?.showMsg("saved at $path")
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
		}
	}

	companion object {
		const val TAG = "MAIN PRESENTER"
	}
}