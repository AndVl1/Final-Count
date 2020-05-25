package com.techpark.finalcount.plans.views

import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.techpark.finalcount.R
import com.techpark.finalcount.data.room.model.Planning
import kotlinx.android.synthetic.main.planning_list_elem.view.*
import java.util.*

class PlansHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
	var element: RelativeLayout = itemView.findViewById(R.id.planning_list_element)
	var id: Long = 0

	fun bind(planning: Planning) {
		id = planning.id
		element.start_date.text = "${Date(planning.begin).date}.${Date(planning.begin).month}-"
		element.end_date.text = "${Date(planning.end).date}.${Date(planning.end).month}"
		element.left_for_plan.text = (planning.planned - planning.spent).toString()
	}

	init {
//		element.setOnClickListener { v ->
//			Log.d("HOLDER", "click")
//			val intent = Intent(v.context, PurchaseActivity::class.java)
//			intent.putExtra("id", id)
//			v.context.startActivity(intent)
//		}
	}
}