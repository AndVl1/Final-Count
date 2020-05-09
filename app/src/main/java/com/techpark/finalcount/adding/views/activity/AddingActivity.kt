package com.techpark.finalcount.adding.views.activity


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import com.techpark.finalcount.App
import com.techpark.finalcount.R
import com.techpark.finalcount.adding.presenters.AddingPresenterImplementation
import com.techpark.finalcount.adding.views.AddingView
import com.techpark.finalcount.base.BaseActivity
import com.techpark.finalcount.databinding.ActivityAddingBinding
import com.techpark.finalcount.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


class AddingActivity : BaseActivity(), AddingView {

    private lateinit var mAddingBinding: ActivityAddingBinding
    private val activityJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + activityJob)
    private val s = StringBuilder()
    private lateinit var currencies: Array<String>
    private lateinit var currency: String

    @Inject
    lateinit var mAddingPresenter: AddingPresenterImplementation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAddingBinding = ActivityAddingBinding.inflate(layoutInflater)
        setContentView(mAddingBinding.root)

        (applicationContext as App).appComponent.inject(this)

        currencies = resources.getStringArray(R.array.currencies)

        mAddingPresenter.attachView(this)

        mAddingBinding.submit.setOnClickListener {
            mAddingPresenter.add(
                mAddingBinding.name.text.toString(),
                mAddingBinding.price.text.toString().toInt(),
                currency
            )
            mAddingBinding.name.text.clear()
            mAddingBinding.price.text.clear()
        }

        mAddingBinding.check.setOnClickListener {
            scope.launch {
                Log.d("CHECK", "mother")
                val job = launch {
                    Log.d("CHECK", "child")
                    mAddingPresenter.check()
                }
                job.join()
                Log.d("CHECK", s.toString())
                mAddingBinding.list.text = s
                s.clear()
            }

        }


        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item)
        mAddingBinding.spinner.adapter = adapter
        mAddingBinding.spinner.prompt = getString(R.string.choose_currency)
        mAddingBinding.spinner.setSelection(1)
        mAddingBinding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View,
                position: Int, id: Long
            ) { // показываем позиция нажатого элемента
                Toast.makeText(baseContext, "Position = $position", Toast.LENGTH_SHORT)
                    .show()
                currency = currencies[position]
                mAddingBinding.currency.text = currency
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
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
