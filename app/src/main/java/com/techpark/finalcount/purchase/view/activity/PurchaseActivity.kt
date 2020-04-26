package com.techpark.finalcount.purchase.view.activity

import android.os.Bundle
import com.techpark.finalcount.database.model.Purchase
import com.techpark.finalcount.databinding.ActivityPurchaseBinding
import com.techpark.finalcount.purchase.presenter.PurchasePresenterImpl
import com.techpark.finalcount.purchase.view.PurchaseView
import dagger.android.support.DaggerAppCompatActivity
import java.util.*
import javax.inject.Inject

class PurchaseActivity: DaggerAppCompatActivity(), PurchaseView {
    private lateinit var mPurchaseBinding: ActivityPurchaseBinding

    @Inject
    lateinit var mPurchasePresenter: PurchasePresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPurchaseBinding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(mPurchaseBinding.root)
        mPurchasePresenter.attachView(this)
        val intent = intent
        val id = intent.getLongExtra("id", 0)
        mPurchasePresenter.init(id)

        mPurchaseBinding.delete.setOnClickListener {
            mPurchasePresenter.delete()
            onBackPressed()
        }

//        mPurchaseBinding.name.text = mPurchasePresenter.getName()
//        mPurchaseBinding.price.text = mPurchasePresenter.getPrice().toString()
//        mPurchaseBinding.currency.text = mPurchasePresenter.getCurrency()
//        mPurchaseBinding.date.text = Date(mPurchasePresenter.getDate()).toString()
    }

    override fun setParams(purchase: Purchase) {
        mPurchaseBinding.name.text = purchase.name
        mPurchaseBinding.price.text = purchase.cost.toString()
        mPurchaseBinding.currency.text = purchase.currency
        mPurchaseBinding.date.text = Date(purchase.date).toString()
    }
}