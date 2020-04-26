package com.techpark.finalcount.purchase.view

import com.techpark.finalcount.base.BaseView
import com.techpark.finalcount.database.model.Purchase

interface PurchaseView: BaseView {
//    fun initPresenter(id: Long)
    fun setParams(purchase: Purchase)
}