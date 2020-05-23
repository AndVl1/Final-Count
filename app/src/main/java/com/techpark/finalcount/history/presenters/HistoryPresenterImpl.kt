package com.techpark.finalcount.history.presenters

import androidx.lifecycle.MutableLiveData
import com.techpark.finalcount.base.BasePresenterImpl
import com.techpark.finalcount.data.DataSource
import com.techpark.finalcount.history.ListElement
import com.techpark.finalcount.history.views.HistoryView
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryPresenterImpl @Inject constructor(private val dataSource: DataSource): HistoryPresenter, BasePresenterImpl<HistoryView>() {

	private var liveData = MutableLiveData<ListElement>()
	private val mPurchaseArray = ArrayList<ListElement>()

	override fun observeForPurchaseChanging() = liveData

	@InternalCoroutinesApi
	override fun getPurchases() {
		try {
			mMainScope.launch {
				mIOScope.launch {
					mPurchaseArray.clear()

					dataSource.database.purchaseDao().loadAll().collect { print(it) }
				}.join()
				mView?.setupViewContent(mPurchaseArray)
			}
		} catch (e: Exception) {
		}
	}

}