package com.techpark.finalcount.history.views.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techpark.finalcount.R
import com.techpark.finalcount.base.BaseFragment
import com.techpark.finalcount.history.viewmodel.HistoryViewModel
import com.techpark.finalcount.history.views.HistoryPagedAdapter
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Couldn't find how to
 * observe database from presenter
 * so did this fragment with ViewModel
 * */

class HistoryFragmentMvvm: BaseFragment() {

	private var mRoot: View? = null
	private var mRecyclerView: RecyclerView? = null
	private var mLinearLayoutManager : LinearLayoutManager? = null

	@Inject
	lateinit var mViewModel: HistoryViewModel

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		if (mRoot == null) {
			mRoot = inflater.inflate(R.layout.fragment_history, container, false)
			mRecyclerView = mRoot?.findViewById(R.id.purchasesList)
			mLinearLayoutManager = LinearLayoutManager(this.context)
			mRecyclerView?.layoutManager = mLinearLayoutManager

			val adapter = HistoryPagedAdapter()
			mViewModel.mPurchaseList.observe(viewLifecycleOwner, Observer {
				adapter.submitList(it)
			})
			mRecyclerView?.adapter = adapter
		}


		return mRoot
	}

	override fun onAttach(context: Context) {
		AndroidSupportInjection.inject(this)
		super.onAttach(context)
	}

	companion object {
		fun newInstance(): BaseFragment = HistoryFragmentMvvm()
	}
}