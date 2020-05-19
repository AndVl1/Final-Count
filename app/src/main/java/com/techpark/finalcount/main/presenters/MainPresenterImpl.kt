package com.techpark.finalcount.main.presenters

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.techpark.finalcount.base.BasePresenterImpl
import com.techpark.finalcount.data.DataSource
import com.techpark.finalcount.data.dbexpimp.JsonDbExportImportApiKt
import com.techpark.finalcount.main.views.MainView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainPresenterImpl @Inject constructor(private val dataSource: DataSource): MainPresenter, BasePresenterImpl<MainView>() {
	override fun saveAll(root: String) {
		mMainScope.launch {
			var path = ""
			val jsonArray = JsonDbExportImportApiKt.exportPurchaseDbToJsonArray(dataSource.database.purchaseDao())
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
		val auth = FirebaseAuth.getInstance()
		auth.signOut()
		mView?.toAuthActivity()
	}

	companion object {
		const val TAG = "MAIN PRESENTER"
	}
}