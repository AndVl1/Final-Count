package com.techpark.finalcount.adding.plan.presenter

import com.techpark.finalcount.adding.plan.views.AddingPlanningView
import com.techpark.finalcount.base.BasePresenterImpl
import com.techpark.finalcount.data.DataSource
import com.techpark.finalcount.data.room.model.Planning
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddingPlanningPresenterImpl @Inject constructor(dataSource: DataSource): BasePresenterImpl<AddingPlanningView>(),
	AddingPlanningPresenter {
	private var mBeginningDate : Long = -1
	private var mEndingDate : Long = -1
	private val mPurchaseDao = dataSource.purchaseDatabase.purchaseDao()
	private val mPlanningDao = dataSource.planningDatabase.planningDao()
	val format = SimpleDateFormat("dd.mm.YYYY", Locale.getDefault())

	override fun addPlannedDates(start: Long, end: Long) {
		mBeginningDate = start
		mEndingDate = end
		val dateStart = Date(start)
		val dateEnd = Date(end)
		mView?.showDates(format.format(dateStart), format.format(dateEnd))
	}

	override fun submit(amount: Int) {
		mMainScope.launch {
			val list = withContext(mIOScope.coroutineContext) {
				mPurchaseDao.getByMonth(mBeginningDate, mEndingDate)
			}
			var sum = 0
			for (elem in list) {
				sum += elem.cost
			}
			mIOScope.launch {
				mPlanningDao.insert(Planning(0, mBeginningDate, mEndingDate, amount, sum))
			}
			mView?.planSuccess()
		}
	}
}