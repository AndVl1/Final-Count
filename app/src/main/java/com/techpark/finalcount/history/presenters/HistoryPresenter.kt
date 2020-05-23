package com.techpark.finalcount.history.presenters

import androidx.lifecycle.LiveData
import com.techpark.finalcount.base.BasePresenter
import com.techpark.finalcount.history.ListElement
import com.techpark.finalcount.history.views.HistoryView

interface HistoryPresenter : BasePresenter<HistoryView> {
	fun getPurchases()
	fun observeForPurchaseChanging(): LiveData<ListElement>
}