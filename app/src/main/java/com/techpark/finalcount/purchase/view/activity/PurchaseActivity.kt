package com.techpark.finalcount.purchase.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.techpark.finalcount.base.BaseActivity
import com.techpark.finalcount.data.room.model.Purchase
import com.techpark.finalcount.databinding.ActivityPurchaseBinding
import com.techpark.finalcount.purchase.presenter.PurchasePresenterImpl
import com.techpark.finalcount.purchase.view.PurchaseView
import dagger.android.AndroidInjection
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PurchaseActivity: BaseActivity(), PurchaseView {
	private lateinit var mPurchaseBinding: ActivityPurchaseBinding
	private val mFormat = SimpleDateFormat("dd.mm.YYYY HH:mm", Locale.getDefault())
	
	@Inject
	lateinit var mPurchasePresenter: PurchasePresenterImpl
	
	override fun onCreate(savedInstanceState: Bundle?) {
		AndroidInjection.inject(this)
		super.onCreate(savedInstanceState)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		mPurchaseBinding = ActivityPurchaseBinding.inflate(layoutInflater)
		setContentView(mPurchaseBinding.root)
		mPurchasePresenter.attachView(this)
		val intent = intent
		val date = intent.getLongExtra("date", 0)
		mPurchasePresenter.getPurchase(date)

		
		mPurchaseBinding.delete.setOnClickListener {
			mPurchasePresenter.delete()
			onBackPressed()
		}
		
		mPurchaseBinding.redact.setOnClickListener {
			mPurchaseBinding.redactLayout.visibility = View.VISIBLE
		}
		
		mPurchaseBinding.updateBtn.setOnClickListener {
			mPurchasePresenter.update(
				mPurchaseBinding.newName.text.toString(),
				mPurchaseBinding.newPrice.text.toString()
			)
			mPurchasePresenter.getPurchase(date)
			mPurchaseBinding.redactLayout.visibility = View.GONE
			mPurchaseBinding.newName.text.clear()
			mPurchaseBinding.newPrice.text.clear()
		}
	}
	
	override fun setParams(purchase: Purchase) {
		mPurchaseBinding.name.text = purchase.name
		mPurchaseBinding.price.text = purchase.cost.toString()
		mPurchaseBinding.date.text = mFormat.format(purchase.date)
		mPurchaseBinding.redactLayout.visibility = View.GONE
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean =
		when (item.itemId) {
			android.R.id.home -> {
				onBackPressed()
				true
			}
			else -> true
		}
	
}