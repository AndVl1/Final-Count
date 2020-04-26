package com.techpark.finalcount.history.presenters

import android.content.Context
import android.widget.ArrayAdapter
import com.techpark.finalcount.R
import com.techpark.finalcount.database.DataSource
import com.techpark.finalcount.history.ListElement
import com.techpark.finalcount.history.views.HistoryView
import kotlinx.android.synthetic.main.history_list_elem.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryPresenterImplementation @Inject constructor(private val dataSource: DataSource):
    HistoryPresenter {
    private var mHistoryView: HistoryView? = null
//    private val mPresenterJob = Job()
    private val mScope = CoroutineScope(IO )
    private var mPurchaseArray = ArrayList<ListElement>()

    override fun getPurchases() {
        try {
            MainScope().launch {
                val job = mScope.launch {
                    for (s in dataSource.database.purchaseDao().loadAll()) {
                        mPurchaseArray.add(ListElement(s.id, s.name, s.cost, s.currency))
                    }
                }
                job.join()
                mHistoryView?.setupViewContent(mPurchaseArray)
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