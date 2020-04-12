package com.techpark.finalcount.adding.views.activity


import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.techpark.finalcount.adding.presenters.AddingPresenterImplementation
import com.techpark.finalcount.adding.views.AddingView
import com.techpark.finalcount.databinding.ActivityAddingBinding
import com.techpark.finalcount.utils.Utils
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.*
import javax.inject.Inject

class AddingActivity : DaggerAppCompatActivity(), AddingView {

    private lateinit var mAddingBinding: ActivityAddingBinding
    private val activityJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + activityJob)
    private val s = StringBuilder()

    @Inject
    lateinit var mAddingPresenter: AddingPresenterImplementation

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAddingBinding = ActivityAddingBinding.inflate(layoutInflater)
        setContentView(mAddingBinding.root)

        mAddingPresenter.attachView(this)

        mAddingBinding.submit.setOnClickListener {
            mAddingPresenter.add(
                mAddingBinding.name.text.toString(),
                mAddingBinding.price.text.toString().toInt(),
                java.util.Currency.getInstance(mAddingBinding.currency.text.toString()).numericCode
            )
        }

        mAddingBinding.check.setOnClickListener {
            scope.launch {
                mAddingPresenter.check()
                delay(1000)
                mAddingBinding.list.text = s
                s.clear()
            }
        }
    }

    override fun addDebugText(string: String) {
        s.append(string)
    }

    override fun showError(error: String) {
        mAddingBinding.statusView.text = error
        mAddingBinding.statusView.visibility = View.VISIBLE
    }

    override fun addSuccess() {
        Utils.showMessage(this, "success")
        setLoadingVisibility(false)
    }

    override fun addFailed() {
        Utils.showMessage(this, "error")
        setLoadingVisibility(false)
    }

    override fun setLoadingVisibility(visibility: Boolean) {
        if (visibility)
            mAddingBinding.progressBar.visibility = View.VISIBLE
        else mAddingBinding.progressBar.visibility = View.GONE
    }
}
