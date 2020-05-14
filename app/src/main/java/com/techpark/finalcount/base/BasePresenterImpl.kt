package com.techpark.finalcount.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class BasePresenterImpl<V: BaseView>: BasePresenter<V> {
	var mView : V? = null

	val mJob = Job()
	var mMainScope = CoroutineScope(Dispatchers.Main + mJob)
	val mIOScope = CoroutineScope(Dispatchers.IO + mJob)

	override fun attachView(view: V) {
		this.mView = view
	}

	override fun detachView() {
		this.mView = null
		mJob.cancel()
	}
}