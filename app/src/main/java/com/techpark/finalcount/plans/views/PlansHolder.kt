package com.techpark.finalcount.plans.views

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.techpark.finalcount.R
import com.techpark.finalcount.data.room.model.Planning
import kotlinx.android.synthetic.main.planning_list_elem.view.*
import java.text.SimpleDateFormat
import java.util.*

class PlansHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
	private var mElement: CardView = itemView.findViewById(R.id.planning_list_element)
	private var mDate: Long = 0
	private val mFormat = SimpleDateFormat("dd.mm.YYYY", Locale.getDefault())

	@SuppressLint("SetTextI18n")
	fun bind(planning: Planning) {
		mDate = planning.id
		mElement.start_date.text = mFormat.format(planning.begin)
		mElement.end_date.text = " - " + mFormat.format(planning.end)
		val left = planning.planned - planning.spent
		mElement.left_for_plan.text = left.toString()
		mElement.total_spent.text = planning.spent.toString()
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (left > 0) {
				mElement.left_for_plan.setTextColor(itemView.context.getColor(R.color.green))
			} else {
				mElement.left_for_plan.setTextColor(itemView.context.getColor(R.color.red))
			}
		}
	}
}