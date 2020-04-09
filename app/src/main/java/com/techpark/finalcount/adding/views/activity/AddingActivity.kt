package com.techpark.finalcount.adding.views.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.techpark.finalcount.adding.views.AddingView
import com.techpark.finalcount.databinding.ActivityAddingBinding
import com.techpark.finalcount.utils.Utils

class AddingActivity : AppCompatActivity(), AddingView {

    private lateinit var mAddingBinding: ActivityAddingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAddingBinding = ActivityAddingBinding.inflate(layoutInflater)
        setContentView(mAddingBinding.root)
    }

    override fun showError(error: String) {
        mAddingBinding.statusView.text = error
        mAddingBinding.statusView.visibility = View.VISIBLE
    }

    override fun addSuccess() {
        Utils.showMessage(this, "success")
    }

    override fun addFailed() {
        Utils.showMessage(this, "error")
    }

    override fun setLoadingVisibility(visibility: Boolean) {
        if (visibility)
            mAddingBinding.progressBar.visibility = View.VISIBLE
        else mAddingBinding.progressBar.visibility = View.GONE
    }
}
