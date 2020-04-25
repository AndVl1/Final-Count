package com.techpark.finalcount.adding.presenters

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.techpark.finalcount.adding.views.AddingView
import com.techpark.finalcount.database.DataSource
import com.techpark.finalcount.database.model.Purchase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class AddingPresenterImplementation @Inject constructor(private val dataSource: DataSource): AddingPresenter {
    private var mAddingView: AddingView? = null
    private val mPresenterJob = Job()
    private val mScope = CoroutineScope(IO + mPresenterJob)
    private val mAuth = FirebaseAuth.getInstance()
    private val mFirebaseStorage = FirebaseStorage.getInstance()

    override fun add(name: String, cost: Int, currency: String) {
        mAddingView?.setLoadingVisibility(true)
        val purchase = Purchase(0, name, cost, currency, System.currentTimeMillis())
        try {
            mScope.launch {
                dataSource.database.purchaseDao().insert(purchase)
            }
            mAddingView?.addSuccess()
        } catch (e: Exception) {
            mAddingView?.addFailed()
            mAddingView?.showError(e.localizedMessage ?: "Unresolved error")
        }
    }

    override fun check(): String {
        return try {
            Log.d("PR", "check")
            mScope.launch {
                val list = dataSource.database.purchaseDao().loadAll()
                for (p in list) {
                    mAddingView?.addDebugText("${p.name} - ${p.cost} ${p.currency} (${Date(p.date)})\n")
                }
            }
            ""
        } catch (e: Exception) {
            "error"
        }
    }

    override fun attachView(view: AddingView) {
        mAddingView = view
    }

    override fun detachView() {
        mAddingView = null
    }
}