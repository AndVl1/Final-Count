package com.techpark.finalcount.adding.purchase.views.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.heyalex.bottomdrawer.BottomDrawerDialog
import com.github.heyalex.bottomdrawer.BottomDrawerFragment
import com.github.heyalex.handle.PlainHandleView
import com.google.android.material.textfield.TextInputLayout
import com.techpark.finalcount.R
import com.techpark.finalcount.adding.purchase.presenters.AddingPurchasePresenterImpl
import com.techpark.finalcount.adding.purchase.views.AddingPurchaseView
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AddingPurchaseFragment: AddingPurchaseView, BottomDrawerFragment() {

	private var mAlphaCancelButton = 0f
	private lateinit var mCancelButton: ImageView
	private lateinit var mNameTextLayout: TextInputLayout
	private lateinit var mNameText: EditText

	private lateinit var mPriceTextLayout: TextInputLayout
	private lateinit var mPriceText: EditText

	@Inject
	lateinit var mAddingPresenter: AddingPurchasePresenterImpl

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val mRoot = inflater.inflate(R.layout.adding_purchase_fragment, container, false)
		mCancelButton = mRoot.findViewById(R.id.cancel)
		val percent = 0.65f

		mNameText = mRoot.findViewById(R.id.name_textInput)
		mPriceText = mRoot.findViewById(R.id.price_textInput)
		mNameTextLayout = mRoot.findViewById(R.id.name_layout)
		mPriceTextLayout = mRoot.findViewById(R.id.price_layout)

		addBottomSheetCallback {
			onSlide { _, slideOffset ->
				val alphaTemp = (slideOffset - percent) * (1f / (1f - percent))
				mAlphaCancelButton = if (alphaTemp >= 0) {
					alphaTemp
				} else {
					0f
				}
				mCancelButton.alpha = mAlphaCancelButton
				mCancelButton.isEnabled = mAlphaCancelButton > 0
			}
		}
		mCancelButton.setOnClickListener {
			dismissWithBehavior()
		}

		mNameText.setOnFocusChangeListener { _, hasFocus ->
			if (hasFocus) {
				mNameTextLayout.error = EMPTY_TEXT
			}
		}

		mPriceText.setOnFocusChangeListener { _, hasFocus ->
			if (hasFocus)
				mPriceTextLayout.error = EMPTY_TEXT
		}

		mRoot.findViewById<Button>(R.id.submit_adding_button)
			.setOnClickListener {
				if (areInputFieldsFilled())
					mAddingPresenter.add(mNameText.text.toString(), mPriceText.text.toString().toInt(), "RUB")
				else {
					if (mNameText.text.isEmpty()) {
						mNameTextLayout.error = getString(R.string.adding_empty)
					}
					if (mPriceText.text.isEmpty()) {
						mPriceTextLayout.error = getString(R.string.adding_empty)
					}
				}
			}

		return mRoot
	}

	private fun areInputFieldsFilled(): Boolean =
		mNameText.text.isNotEmpty() &&
				mPriceText.text.isNotEmpty()

	override fun configureBottomDrawer(): BottomDrawerDialog {
		return BottomDrawerDialog.build(requireContext()) {
			theme = R.style.Plain
			handleView = PlainHandleView(context).apply {
				val widthHandle =
					resources.getDimensionPixelSize(R.dimen.bottom_sheet_handle_width)
				val heightHandle =
					resources.getDimensionPixelSize(R.dimen.bottom_sheet_handle_height)
				val params =
					FrameLayout.LayoutParams(widthHandle, heightHandle, Gravity.CENTER_HORIZONTAL)

				params.topMargin =
					resources.getDimensionPixelSize(R.dimen.bottom_sheet_handle_top_margin)

				layoutParams = params
			}
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putFloat("alphaCancelButton", mAlphaCancelButton)
	}

	override fun onViewStateRestored(savedInstanceState: Bundle?) {
		super.onViewStateRestored(savedInstanceState)
		mAlphaCancelButton = savedInstanceState?.getFloat("alphaCancelButton") ?: 0f
		mCancelButton.alpha = mAlphaCancelButton
		mCancelButton.isEnabled = mAlphaCancelButton > 0
	}

	override fun onAttach(context: Context) {
		AndroidSupportInjection.inject(this)
		super.onAttach(context)
		Log.d(TAG, "attach")
		mAddingPresenter.attachView(this)
	}

	override fun onDestroy() {
		mAddingPresenter.detachView()
		super.onDestroy()
		Log.d(TAG, "destroy")
	}

	override fun showError(error: String) {
		val statusText = view?.findViewById<TextView>(R.id.status_view)
		statusText?.text = error
		statusText?.visibility = View.VISIBLE
	}

	override fun addSuccess() {
		dismissWithBehavior()

	}

	override fun addFailed() {
		mNameTextLayout.error = "error"
		mPriceTextLayout.error = "error"
		// TODO error styles
	}

	override fun setLoadingVisibility(visibility: Boolean) {

	}

	companion object {
		const val EMPTY_TEXT = ""
		const val TAG = "AD PUR FR"
	}
}