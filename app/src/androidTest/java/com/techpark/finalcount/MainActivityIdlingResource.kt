package com.techpark.finalcount

import android.view.View
import androidx.test.espresso.IdlingResource
import com.techpark.finalcount.base.BaseActivity
import com.techpark.finalcount.main.views.activity.MainActivity

class MainActivityIdlingResource (activity: MainActivity) : IdlingResource {
	private var mActivity: BaseActivity = activity
	private var mCallback: IdlingResource.ResourceCallback? = null

	override fun getName(): String =
		TAG + mActivity.localClassName

	override fun isIdleNow(): Boolean {
		val bar: View = mActivity.findViewById(R.id.fab)
		val idle =
			bar.visibility == View.VISIBLE
		if (mCallback != null && idle) {
			mCallback!!.onTransitionToIdle()
		}
		return idle
	}

	override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
		mCallback = callback
	}

	companion object {
		const val TAG = "MainActivityIdlingResource"
	}
}