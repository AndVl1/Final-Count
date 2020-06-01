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
	var element: CardView = itemView.findViewById(R.id.planning_list_element)
	var date: Long = 0
	private val format = SimpleDateFormat("dd.mm.YYYY", Locale.getDefault())

	@SuppressLint("SetTextI18n")
	fun bind(planning: Planning) {
		date = planning.id
		element.start_date.text = format.format(planning.begin)
		element.end_date.text = " - " + format.format(planning.end)
		val left = planning.planned - planning.spent
		element.left_for_plan.text = left.toString()
		element.total_spent.text = planning.spent.toString()
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (left > 0) {
				element.left_for_plan.setTextColor(itemView.context.getColor(R.color.green))
			} else {
				element.left_for_plan.setTextColor(itemView.context.getColor(R.color.red))
			}
		}
	}
}