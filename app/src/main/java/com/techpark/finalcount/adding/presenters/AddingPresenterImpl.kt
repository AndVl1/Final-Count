package com.techpark.finalcount.adding.presenters

import com.techpark.finalcount.adding.views.AddingView
import com.techpark.finalcount.base.BasePresenterImpl
import com.techpark.finalcount.data.DataSource
import com.techpark.finalcount.data.room.model.Purchase
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddingPresenterImpl @Inject constructor(private val dataSource: DataSource): AddingPresenter, BasePresenterImpl<AddingView>() {
//    private var mAddingView: AddingView? = null


	override fun add(name: String, cost: Int, currency: String) {
		mView?.setLoadingVisibility(true)
		val purchase = Purchase(0, name, cost, currency, System.currentTimeMillis())
		mMainScope.launch {
			try {
				mIOScope.launch {
					dataSource.database.purchaseDao().insert(purchase)
				}.join()
				mView?.addSuccess()
			} catch (e: Exception) {
				mView?.addFailed()
				mView?.showError(e.localizedMessage ?: "Unresolved error")
			}
		}
	}

	override fun attachView(view: AddingView) {
		mView = view
	}

	override fun detachView() {
		mView = null
	}
}