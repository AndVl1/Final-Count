package com.techpark.finalcount.history.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.techpark.finalcount.R
import com.techpark.finalcount.database.model.Purchase
import com.techpark.finalcount.history.presenters.HistoryPresenterImplementation
import com.techpark.finalcount.history.views.HistoryView
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HistoryFragment : DaggerFragment(), HistoryView {

    private var root: View? = null
    private var tv: TextView? = null

    @Inject
    lateinit var mHistoryPresenter: HistoryPresenterImplementation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_history, container, false)
            tv = root?.findViewById(R.id.purchases)
        }
        mHistoryPresenter.attachView(this)
        mHistoryPresenter.getPurchases()
        return root
    }

    override fun addPurchase(s: Purchase) {
        tv?.append(s.name + "(" + s.currency + ", " + s.date + ") - " + s.cost + "\n")
    }

    override fun onDestroy() {
        super.onDestroy()

        mHistoryPresenter.detachView()
        tv = null
    }

}
