package com.techpark.finalcount.history.presenters

import com.techpark.finalcount.database.DataSource
import com.techpark.finalcount.history.views.HistoryView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryPresenterImplementation @Inject constructor(private val dataSource: DataSource):
    HistoryPresenter {
    private var mHistoryView: HistoryView? = null
    private val mPresenterJob = Job()
    private val mScope = CoroutineScope(IO + mPresenterJob)

    override fun getPurchases() {
        try {
            mScope.launch {
                for (s in dataSource.database.purchaseDao().loadAll()) {
                    mHistoryView?.addPurchase(s)
                }
            }
        } catch (e: Exception) {
        }
    }

    override fun attachView(view: HistoryView) {
        mHistoryView = view
    }

    override fun detachView() {
        mHistoryView = null
    }

}