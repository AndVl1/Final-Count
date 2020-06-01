package com.techpark.finalcount.history.views

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.techpark.finalcount.R
import com.techpark.finalcount.data.room.model.Purchase
import com.techpark.finalcount.purchase.view.activity.PurchaseActivity
import kotlinx.android.synthetic.main.history_list_elem.view.*

class HistoryHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
	var element: CardView = itemView.findViewById(R.id.history_list_element)
	var date: Long = 0

	init {
		element.setOnClickListener { v ->
			Log.d("HOLDER", "click")
			val intent = Intent(v.context, PurchaseActivity::class.java)
			intent.putExtra("date", date)
			v.context.startActivity(intent)
		}
	}

	fun bind(purchase: Purchase) {
		date = purchase.date
		element.purchase_name.text = purchase.name
		element.purchase_cost.text = purchase.cost.toString()
	}
}