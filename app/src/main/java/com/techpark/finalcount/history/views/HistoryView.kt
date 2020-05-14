package com.techpark.finalcount.history.views

import com.techpark.finalcount.base.BaseView
import com.techpark.finalcount.history.ListElement

interface HistoryView: BaseView {
    fun setupViewContent(list: ArrayList<ListElement>)
}