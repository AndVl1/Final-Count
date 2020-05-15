package com.techpark.finalcount.purchase.view.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.techpark.finalcount.R
import com.techpark.finalcount.base.BaseActivity
import com.techpark.finalcount.data.room.model.Purchase
import com.techpark.finalcount.databinding.ActivityPurchaseBinding
import com.techpark.finalcount.purchase.presenter.PurchasePresenterImpl
import com.techpark.finalcount.purchase.view.PurchaseView
import dagger.android.AndroidInjection
import java.util.*
import javax.inject.Inject

class PurchaseActivity: BaseActivity(), PurchaseView {
	private lateinit var mPurchaseBinding: ActivityPurchaseBinding
	private lateinit var currencies: Array<String>
	private lateinit var currency : String

	@Inject
	lateinit var mPurchasePresenter: PurchasePresenterImpl

	override fun onCreate(savedInstanceState: Bundle?) {
		AndroidInjection.inject(this)
		super.onCreate(savedInstanceState)
		mPurchaseBinding = ActivityPurchaseBinding.inflate(layoutInflater)
		setContentView(mPurchaseBinding.root)
		mPurchasePresenter.attachView(this)
		val intent = intent
		val id = intent.getLongExtra("id", 0)
		mPurchasePresenter.getPurchase(id)

		currencies =  resources.getStringArray(R.array.currencies)

		mPurchaseBinding.delete.setOnClickListener {
			mPurchasePresenter.delete()
			onBackPressed()
		}

		mPurchaseBinding.redact.setOnClickListener {
			mPurchaseBinding.redactLayout.visibility = View.VISIBLE
		}

		mPurchaseBinding.updateBtn.setOnClickListener {
			mPurchasePresenter.update(mPurchaseBinding.newName.text.toString(),
				mPurchaseBinding.newPrice.text.toString().toInt(), currency)
			mPurchasePresenter.getPurchase(id)
			mPurchaseBinding.redactLayout.visibility = View.GONE
			mPurchaseBinding.newName.text.clear()
			mPurchaseBinding.newPrice.text.clear()
		}


		val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencies)
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item)
		mPurchaseBinding.spinner.adapter = adapter
		mPurchaseBinding.spinner.prompt = getString(R.string.choose_currency)
		mPurchaseBinding.spinner.setSelection(1)
		mPurchaseBinding.spinner.onItemSelectedListener = object :
			AdapterView.OnItemSelectedListener {
			override fun onItemSelected(
				parent: AdapterView<*>?, view: View,
				position: Int, id: Long
			) { // показываем позиция нажатого элемента
				Toast.makeText(baseContext, "Position = $position", Toast.LENGTH_SHORT)
					.show()
				currency = currencies[position]
			}

			override fun onNothingSelected(arg0: AdapterView<*>?) {}
		}
	}

	override fun setParams(purchase: Purchase) {
		mPurchaseBinding.name.text = purchase.name
		mPurchaseBinding.price.text = purchase.cost.toString()
		mPurchaseBinding.currency.text = purchase.currency
		mPurchaseBinding.date.text = Date(purchase.date).toString()
	}
}