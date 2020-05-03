package com.techpark.finalcount.pincode.presenter

import com.techpark.finalcount.base.BasePresenter
import com.techpark.finalcount.pincode.views.PincodeView

interface PincodePresenter: BasePresenter<PincodeView> {
    fun addNumber(num: String)
    fun clear()
    fun handleScanner(success: Boolean)
    fun isLogin(): Boolean
}