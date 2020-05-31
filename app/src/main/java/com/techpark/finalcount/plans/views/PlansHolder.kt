package com.techpark.finalcount.plans.views

import android.annotation.SuppressLint
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
	var id: Long = 0
	val format = SimpleDateFormat("dd.mm.YYYY", Locale.getDefault())

	@SuppressLint("SetTextI18n")
	fun bind(planning: Planning) {
		id = planning.id
		element.start_date.text = format.format(planning.begin)
		element.end_date.text = " - " + format.format(planning.end)
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