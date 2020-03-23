package com.techpark.finalcount.base

interface BasePresenter<v: BaseView> {
    fun attachView(view: v)
    fun detachView()
}