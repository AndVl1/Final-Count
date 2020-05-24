package com.techpark.finalcount.history.views

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techpark.finalcount.R
import com.techpark.finalcount.history.ListElement
import kotlinx.android.synthetic.main.history_list_elem.view.*

class HistoryAdapter(): RecyclerView.Adapter<HistoryHolder>() {
	private var context: Context? = null
	private lateinit var list: ArrayList<ListElement>

	constructor(context: Context?, list: ArrayList<ListElement>) : this() {
		this.context = context
		this.list = list
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.history_list_elem, parent, false)
		return HistoryHolder(view)
	}

	override fun getItemCount(): Int {
		Log.d("ADAPTER", "${list.size}")
		return list.size
	}

	override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
	    holder.element.purchase_name.text = list[position].name
	    holder.element.purchase_cost.text = context
		    ?.getString(R.string.history_element_price, list[position].price, list[position].currency)
	    holder.id = list[position].id
	}
}