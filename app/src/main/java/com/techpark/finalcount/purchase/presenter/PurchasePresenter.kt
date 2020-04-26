package com.techpark.finalcount.purchase.presenter

import com.techpark.finalcount.base.BasePresenter
import com.techpark.finalcount.purchase.view.PurchaseView

interface PurchasePresenter: BasePresenter<PurchaseView> {
    fun init(id: Long)
    fun delete()
    fun update(name: String, price: Int, currency: String)
}