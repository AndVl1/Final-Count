package com.techpark.finalcount.history.views

import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.techpark.finalcount.R
import kotlinx.android.synthetic.main.history_list_elem.view.*

class HistoryHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val element: RelativeLayout = itemView.findViewById(R.id.history_list_element)

    init {
        element.setOnClickListener { _ ->
            Log.d("HOLDER", "click")
            // TODO open activity
        }
    }
}