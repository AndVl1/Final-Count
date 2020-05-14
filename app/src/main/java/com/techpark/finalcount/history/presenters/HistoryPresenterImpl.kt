package com.techpark.finalcount.history.presenters

import com.techpark.finalcount.base.BasePresenterImpl
import com.techpark.finalcount.database.DataSource
import com.techpark.finalcount.history.ListElement
import com.techpark.finalcount.history.views.HistoryView
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryPresenterImpl @Inject constructor(private val dataSource: DataSource): HistoryPresenter, BasePresenterImpl<HistoryView>() {

	private var mPurchaseArray = ArrayList<ListElement>()

	override fun getPurchases() {
		try {
			mMainScope.launch {
				mIOScope.launch {
					mPurchaseArray.clear()
					for (s in dataSource.database.purchaseDao().loadAll()) {
						mPurchaseArray.add(ListElement(s.id, s.name, s.cost, s.currency))
					}
				}.join()
				mView?.setupViewContent(mPurchaseArray)
			}
		} catch (e: Exception) {
		}
	}

}