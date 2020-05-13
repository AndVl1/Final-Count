package com.techpark.finalcount.purchase.presenter

import android.util.Log
import com.techpark.finalcount.database.DataSource
import com.techpark.finalcount.database.model.Purchase
import com.techpark.finalcount.purchase.view.PurchaseView
import kotlinx.coroutines.*
import javax.inject.Inject

class PurchasePresenterImplementation @Inject constructor(private val dataSource: DataSource): PurchasePresenter {
    private var mPurchaseView: PurchaseView? = null
    private lateinit var mPurchase: Purchase
    private val mJob = Job()
    private var mMainScope = CoroutineScope(Dispatchers.Main + mJob)
    private val mIOScope = CoroutineScope(Dispatchers.IO + mJob)

    override fun getPurchase(id: Long){
        Log.d(TAG, "init $id")
        mMainScope.launch {
            Log.d(TAG, "Main")
            mIOScope.launch {
                getData(id)
            }.join()
            Log.d(TAG, mPurchase.name)
            mPurchaseView?.setParams(mPurchase)
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

    override fun attachView(view: PurchaseView) {
        mPurchaseView = view
    }

    override fun detachView() {
        mPurchaseView = null
        mMainScope.cancel()
    }

    companion object {
        const val TAG = "PURCHASE PRESENTER"
    }
}