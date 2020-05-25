package com.techpark.finalcount.history.views

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.techpark.finalcount.R
import com.techpark.finalcount.data.room.model.Purchase

class HistoryPagedAdapter:
	PagedListAdapter<Purchase, HistoryHolder>(DIFF_CALLBACK) {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
		Log.d(TAG, "onbindviewholder")
		val view = LayoutInflater.from(parent.context).inflate(R.layout.history_list_elem, parent, false)
		return HistoryHolder(view)
	}

	override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
		holder.bind(getItem(position)!!)
	}

	companion object{
		private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Purchase>() {
			override fun areItemsTheSame(oldItem: Purchase, newItem: Purchase): Boolean =
				oldItem.id == newItem.id

			override fun areContentsTheSame(oldItem: Purchase, newItem: Purchase): Boolean =
				oldItem == newItem
		}

		const val TAG = "HISTORY ADAPTER"
	}
}