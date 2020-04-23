package com.techpark.finalcount.adding.presenters

import android.util.Log
import com.techpark.finalcount.adding.views.AddingView
import com.techpark.finalcount.database.DataSource
import com.techpark.finalcount.database.model.Purchase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class AddingPresenterImplementation @Inject constructor(private val dataSource: DataSource): AddingPresenter {
    private var addingView: AddingView? = null
    private val presenterJob = Job()
    private val scope = CoroutineScope(IO + presenterJob)

    override fun add(name: String, cost: Int, currency: String) {
        addingView?.setLoadingVisibility(true)
        val purchase = Purchase(0, name, cost, currency, System.currentTimeMillis())
        try {
            scope.launch {
                dataSource.database.purchaseDao().insert(purchase)
            }
            addingView?.addSuccess()
        } catch (e: Exception) {
            addingView?.addFailed()
            addingView?.showError(e.localizedMessage ?: "Unresolved error")
        }
    }

    override fun check(): String {
        return try {
            Log.d("PR", "check")
            scope.launch {
                val list = dataSource.database.purchaseDao().loadAll()
                for (p in list) {
                    addingView?.addDebugText("${p.name} - ${p.cost} ${p.currency} (${Date(p.date)})\n")
                }
            }
            ""
        } catch (e: Exception) {
            "error"
        }
    }

    override fun attachView(view: AddingView) {
        addingView = view
    }

    override fun detachView() {
        addingView = null
    }
}