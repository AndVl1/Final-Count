package com.techpark.finalcount.adding.plan.presenter

import com.techpark.finalcount.adding.plan.views.AddingPlanningView
import com.techpark.finalcount.base.BasePresenter

interface AddingPlanningPresenter: BasePresenter<AddingPlanningView> {
	fun addPlannedDates(start: Long, end: Long)
	fun submit(amount: Int)
}