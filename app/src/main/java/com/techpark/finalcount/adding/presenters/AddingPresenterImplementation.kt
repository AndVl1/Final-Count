package com.techpark.finalcount.adding.presenters

import com.techpark.finalcount.adding.views.AddingView
import com.techpark.finalcount.database.DataSource
import com.techpark.finalcount.database.model.Purchase
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

class AddingPresenterImplementation @Inject constructor(private val dataSource: DataSource): AddingPresenter {
    private var addingView: AddingView? = null

    @InternalCoroutinesApi
    override suspend fun add(name: String, cost: Int, currency: Int, date: Long) {
        addingView?.setLoadingVisibility(true)
        val purchase = Purchase(0, name, cost, currency, date)
        try {
            dataSource.database.purchaseDao().insert(purchase)
            addingView?.addSuccess()
        } catch (e: Exception) {
            addingView?.addFailed()
            addingView?.showError(e.localizedMessage ?: "Unresolved error")
        }
    }

    override fun attachView(view: AddingView) {
        addingView = view
    }

    override fun detachView() {
        addingView = null
    }
}