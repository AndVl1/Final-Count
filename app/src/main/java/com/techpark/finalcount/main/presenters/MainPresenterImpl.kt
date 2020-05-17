package com.techpark.finalcount.main.presenters

import android.util.Log
import com.techpark.finalcount.base.BasePresenterImpl
import com.techpark.finalcount.data.DataSource
import com.techpark.finalcount.data.dbexpimp.JsonDbExportImportApiKt
import com.techpark.finalcount.main.views.MainView
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainPresenterImpl @Inject constructor(private val dataSource: DataSource): MainPresenter, BasePresenterImpl<MainView>() {
	override fun saveAll(root: String) {
		mMainScope.launch {
			val jsonArray = JsonDbExportImportApiKt.exportPurchaseDbToJsonArray(dataSource.database.purchaseDao())
			JsonDbExportImportApiKt.saveCsv(root, jsonArray)
			Log.d(TAG, jsonArray.toString())
			mView?.showMsg("saved i hope")
		}
	}

	companion object {
		const val TAG = "MAIN PRESENTER"
	}
}