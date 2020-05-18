package com.techpark.finalcount.main.presenters

import com.techpark.finalcount.base.BasePresenter
import com.techpark.finalcount.main.views.MainView

interface MainPresenter: BasePresenter<MainView> {
	fun saveAll(root: String)
}
