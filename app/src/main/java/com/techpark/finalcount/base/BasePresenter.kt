package com.techpark.finalcount.base

interface BasePresenter<V: BaseView> {
	fun attachView(view: V)
	fun detachView()
}
