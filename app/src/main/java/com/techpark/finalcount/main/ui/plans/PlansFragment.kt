package com.techpark.finalcount.main.ui.plans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.techpark.finalcount.R

class PlansFragment : Fragment() {

    private var root: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_plans, container, false)
            /*root = TextView(activity)
            (root as TextView).text = "Dashboard"*/
        }
        return root
    }
}
