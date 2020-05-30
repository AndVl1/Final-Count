package com.techpark.finalcount

import android.view.View
import android.widget.ProgressBar
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback
import com.techpark.finalcount.auth.views.activity.AuthActivity
import com.techpark.finalcount.base.BaseActivity


class AuthActivityIdlingResource(activity: AuthActivity) : IdlingResource {
	private var mActivity: BaseActivity = activity
	private var mCallback: ResourceCallback? = null

	override fun getName(): String {
		return TAG + mActivity.localClassName
	}

	override fun isIdleNow(): Boolean {
		val progressBar: ProgressBar = mActivity.findViewById(R.id.progress_bar)
		val idle =
			progressBar.visibility == View.GONE
		if (mCallback != null && idle) {
			mCallback!!.onTransitionToIdle()
		}
		return idle
	}

	override fun registerIdleTransitionCallback(callback: ResourceCallback) {
		mCallback = callback
	}

	companion object{
		const val TAG = "AuthActivityIdlingResource:"
	}
}