package com.techpark.finalcount.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import com.techpark.finalcount.data.DataSource
import javax.inject.Inject

class HistoryViewModel @Inject constructor(private val dataSource: DataSource): ViewModel() {
	private val mPurchaseDao = dataSource.database.purchaseDao()
	val mPurchaseList = LivePagedListBuilder(mPurchaseDao.loadAll(), 30).build()
}