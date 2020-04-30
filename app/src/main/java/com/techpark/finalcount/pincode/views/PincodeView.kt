package com.techpark.finalcount.pincode.views

import com.techpark.finalcount.base.BaseView

interface PincodeView: BaseView {
    fun pinSuccess()
    fun pinFailed()
    fun addInput(position: Int)
    fun clear()
}