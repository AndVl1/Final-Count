package com.techpark.finalcount.history.presenters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.techpark.finalcount.base.BasePresenterImpl
import com.techpark.finalcount.data.DataSource
import com.techpark.finalcount.data.room.model.Purchase
import com.techpark.finalcount.history.ListElement
import com.techpark.finalcount.history.views.HistoryView
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

class HistoryPresenterImpl @Inject constructor(private val dataSource: DataSource): HistoryPresenter, BasePresenterImpl<HistoryView>() {

	private var mPurchaseDao = dataSource.database.purchaseDao()
	private var liveData = MutableLiveData<ListElement>()
	private val mPurchaseArray = ArrayList<ListElement>()

	private val mPurchaseList: LiveData<PagedList<Purchase>> = LivePagedListBuilder(mPurchaseDao.loadAll(), 30).build()

	override fun observeForPurchaseChanging() = liveData

	@InternalCoroutinesApi
	override fun getPurchases() {

	}

	companion object {
		const val TAG = "HISTORY PR"
	}
}