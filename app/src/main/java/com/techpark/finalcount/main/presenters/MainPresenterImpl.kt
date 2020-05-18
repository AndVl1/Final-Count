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
			var path = ""
			val jsonArray = JsonDbExportImportApiKt.exportPurchaseDbToJsonArray(dataSource.database.purchaseDao())
			mIOScope.launch {
				path = JsonDbExportImportApiKt.saveCsv(root, jsonArray)
			}.join()
			Log.d(TAG, jsonArray.toString())
			mView?.showMsg("saved at $path")
		}
	}

	companion object {
		const val TAG = "MAIN PRESENTER"
	}
}