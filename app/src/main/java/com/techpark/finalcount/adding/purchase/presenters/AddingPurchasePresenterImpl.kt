package com.techpark.finalcount.adding.purchase.presenters

import com.techpark.finalcount.adding.purchase.views.AddingPurchaseView
import com.techpark.finalcount.base.BasePresenterImpl
import com.techpark.finalcount.data.DataSource
import com.techpark.finalcount.data.room.model.Planning
import com.techpark.finalcount.data.room.model.Purchase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddingPurchasePresenterImpl @Inject constructor(dataSource: DataSource): AddingPurchasePresenter, BasePresenterImpl<AddingPurchaseView>() {
//    private var mAddingView: AddingView? = null
	private val mPurchaseDao = dataSource.purchaseDatabase.purchaseDao()
	private val mPlanningDao = dataSource.planningDatabase.planningDao()

	override fun add(name: String, cost: Int, currency: String) {
		mView?.setLoadingVisibility(true)
		val now = System.currentTimeMillis()
		val purchase = Purchase(0, name, cost, currency, now)
		mMainScope.launch {
			try {
				val plans = withContext(mIOScope.coroutineContext) {
					mPlanningDao.loadAllWhereHasDate(now)
				}
				mIOScope.launch {
					mPurchaseDao.insert(purchase)
					for (plan in plans) {
						mPlanningDao.update(Planning
							(
							plan.id,
							plan.begin,
							plan.end,
							plan.planned,
							plan.spent + purchase.cost
						)
						)
					}
				}
				mView?.addSuccess()
			} catch (e: Exception) {
				mView?.addFailed()
				mView?.showError(e.localizedMessage ?: "Unresolved error")
			}
		}
	}

	override fun attachView(view: AddingPurchaseView) {
		mView = view
	}

	override fun detachView() {
		mView = null
	}
}