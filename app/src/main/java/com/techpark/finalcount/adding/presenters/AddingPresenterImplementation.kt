package com.techpark.finalcount.adding.presenters

import com.techpark.finalcount.adding.views.AddingView
import com.techpark.finalcount.database.model.Purchase

class AddingPresenterImplementation: AddingPresenter {
    private var addingView: AddingView? = null
    override fun check() {
        TODO("Not yet implemented")
    }

    override fun add(purchase: Purchase) {
        check()
        TODO("Not yet implemented")
    }

    override fun attachView(view: AddingView) {
        addingView = view
    }

    override fun detachView() {
        addingView = null
    }
}