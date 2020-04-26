package com.techpark.finalcount.history.views

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.techpark.finalcount.R
import com.techpark.finalcount.purchase.view.activity.PurchaseActivity

class HistoryHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val element: RelativeLayout = itemView.findViewById(R.id.history_list_element)
    var id: Long = 0

    init {
        element.setOnClickListener { v ->
            Log.d("HOLDER", "click")
            val intent = Intent(v.context, PurchaseActivity::class.java)
            intent.putExtra("id", id)
            v.context.startActivity(intent)
        }
    }
}