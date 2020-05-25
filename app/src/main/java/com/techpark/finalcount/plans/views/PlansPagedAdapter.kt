package com.techpark.finalcount.plans.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.techpark.finalcount.R
import com.techpark.finalcount.data.room.model.Planning

class PlansPagedAdapter:
	PagedListAdapter<Planning, PlansHolder>(DIFF_CALLBACK) {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlansHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.planning_list_elem, parent, false)
		return PlansHolder(view)
	}

	override fun onBindViewHolder(holder: PlansHolder, position: Int) {
		holder.bind(getItem(position)!!)
	}

	companion object{
		private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Planning>() {
			override fun areItemsTheSame(oldItem: Planning, newItem: Planning): Boolean =
				oldItem.id == newItem.id

			override fun areContentsTheSame(oldItem: Planning, newItem: Planning): Boolean =
				oldItem == newItem
		}
	}
}