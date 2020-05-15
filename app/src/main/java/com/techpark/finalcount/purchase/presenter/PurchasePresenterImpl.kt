package com.techpark.finalcount.purchase.presenter

import android.util.Log
import com.techpark.finalcount.base.BasePresenterImpl
import com.techpark.finalcount.data.DataSource
import com.techpark.finalcount.data.room.model.Purchase
import com.techpark.finalcount.purchase.view.PurchaseView
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class PurchasePresenterImpl @Inject constructor(private val dataSource: DataSource): PurchasePresenter, BasePresenterImpl<PurchaseView>() {
	private lateinit var mPurchase: Purchase

	override fun getPurchase(id: Long){
		Log.d(TAG, "init $id")
		mMainScope.launch {
			Log.d(TAG, "Main")
			mIOScope.launch {
				getData(id)
			}.join()
			Log.d(TAG, mPurchase.name)
			mView?.setParams(mPurchase)
		}
	}

	private suspend fun getData(id: Long) {
		mPurchase = dataSource.database.purchaseDao().getById(id)
		Log.d(TAG, mPurchase.name)
	}

	override fun delete() {
		mIOScope.launch {
			dataSource.database.purchaseDao().delete(mPurchase)
		}
	}

	override fun update(name: String, price: Int, currency: String) {
		mPurchase = Purchase(mPurchase.id,
			if (name.isNotEmpty()) name else mPurchase.name,
			if (price != -1) price else mPurchase.cost,
			currency,
			mPurchase.date
		)
		mIOScope.launch {
			dataSource.database.purchaseDao().update(mPurchase)
		}
	}

	override fun detachView() {
		super.detachView()
		mMainScope.cancel()
	}

	companion object {
		const val TAG = "PURCHASE PRESENTER"
	}
}