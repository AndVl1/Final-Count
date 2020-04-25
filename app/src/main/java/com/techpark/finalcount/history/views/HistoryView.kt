package com.techpark.finalcount.history.views

import com.techpark.finalcount.base.BaseView
import com.techpark.finalcount.database.model.Purchase

interface HistoryView: BaseView {
    fun addPurchase(s: Purchase)
}