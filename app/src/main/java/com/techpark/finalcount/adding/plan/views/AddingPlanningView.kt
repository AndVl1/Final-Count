package com.techpark.finalcount.adding.plan.views

import com.techpark.finalcount.base.BaseView

interface AddingPlanningView: BaseView {
	fun showDates(start: String, end: String)
	fun planSuccess()
}