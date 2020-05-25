package com.techpark.finalcount.purchase.view

import com.techpark.finalcount.base.BaseView
import com.techpark.finalcount.data.room.model.Purchase

interface PurchaseView: BaseView {
//    fun initPresenter(id: Long)
    fun setParams(purchase: Purchase)
}