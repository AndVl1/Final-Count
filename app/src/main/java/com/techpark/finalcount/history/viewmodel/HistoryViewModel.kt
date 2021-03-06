package com.techpark.finalcount.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import com.techpark.finalcount.data.DataSource
import javax.inject.Inject

class HistoryViewModel @Inject constructor(dataSource: DataSource): ViewModel() {
	private val mPurchaseDao = dataSource.purchaseDatabase.purchaseDao()
	val mPurchaseList = LivePagedListBuilder(mPurchaseDao.loadAllAsDataSource(), 20).build()
}