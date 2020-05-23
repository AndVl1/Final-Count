package com.techpark.finalcount.history.views.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techpark.finalcount.R
import com.techpark.finalcount.base.BaseFragment
import com.techpark.finalcount.history.ListElement
import com.techpark.finalcount.history.presenters.HistoryPresenterImpl
import com.techpark.finalcount.history.views.HistoryAdapter
import com.techpark.finalcount.history.views.HistoryView
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class HistoryFragment : BaseFragment(), HistoryView {

	private var mRoot: View? = null
	private var mRecyclerView: RecyclerView? = null
	private var mLinearLayoutManager : LinearLayoutManager? = null

	@Inject
	lateinit var mHistoryPresenter: HistoryPresenterImpl


	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		Log.d(TAG, "onCreateView")
		if (mRoot == null) {
			mRoot = inflater.inflate(R.layout.fragment_history, container, false)
			mRecyclerView = mRoot?.findViewById(R.id.purchasesList)
			mLinearLayoutManager = LinearLayoutManager(this.context)
			mRecyclerView?.layoutManager = mLinearLayoutManager
		}
		mHistoryPresenter.attachView(this)
//        mHistoryPresenter.getPurchases()
		return mRoot
	}

	override fun onAttach(context: Context) {
		AndroidSupportInjection.inject(this)
		super.onAttach(context)
	}

	override fun onResume() {
		super.onResume()
		Log.d(TAG, "onResume")
		mHistoryPresenter.getPurchases()
	}

	override fun setupViewContent(list: ArrayList<ListElement>) {
		Log.d(TAG, "setup adapter")
		val adapter = HistoryAdapter(activity?.applicationContext, list)
		Log.d(TAG, "${adapter.itemCount}")
		mRecyclerView?.adapter = adapter
	}

	override fun onPause() {
		super.onPause()
		Log.d(TAG, "onPause")
	}

	override fun onDestroy() {
		super.onDestroy()
		Log.d(TAG, "onDestroy")

		mHistoryPresenter.detachView()
		mRecyclerView = null
	}

	companion object {
		fun newInstance(): BaseFragment = HistoryFragment()
		const val TAG = "HISTORY FRAGMENT"
	}
}
