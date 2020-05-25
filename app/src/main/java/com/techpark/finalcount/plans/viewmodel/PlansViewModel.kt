package com.techpark.finalcount.plans.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import javax.inject.Inject

class PlansViewModel @Inject constructor(dataSource: com.techpark.finalcount.data.DataSource): ViewModel() {
	private val mPurchaseDao = dataSource.purchaseDatabase.purchaseDao()
	private val mPlanningDao = dataSource.planningDatabase.planningDao()
	val mPlanningDate = LivePagedListBuilder(mPlanningDao.loadAllAsDataSource(), 20).build()
}