package com.techpark.finalcount.history.presenters

import android.content.Context
import com.techpark.finalcount.base.BasePresenter
import com.techpark.finalcount.history.views.HistoryView

interface HistoryPresenter : BasePresenter<HistoryView> {
    fun getPurchases()
}