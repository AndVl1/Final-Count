package com.techpark.finalcount.adding.plan.views.fragment

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.github.heyalex.bottomdrawer.BottomDrawerDialog
import com.github.heyalex.bottomdrawer.BottomDrawerFragment
import com.github.heyalex.handle.PlainHandleView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.techpark.finalcount.R
import com.techpark.finalcount.adding.plan.presenter.AddingPlanningPresenterImpl
import com.techpark.finalcount.adding.plan.views.AddingPlanningView
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AddingPlanningFragment : BottomDrawerFragment(), AddingPlanningView {

	@Inject
	lateinit var mPlanningPresenter: AddingPlanningPresenterImpl

	private var mAlphaCancelButton = 0f

	private lateinit var mLayoutStart: LinearLayout
	private lateinit var mTextViewStart: TextView
	private lateinit var mLayoutEnd: LinearLayout
	private lateinit var mTextViewEnd: TextView
	private lateinit var mSubmitButton: Button
	private lateinit var mRoot: View

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val percent = 0.65f
		mRoot = inflater.inflate(R.layout.addng_plan_fragment, container, false)

		val select: Button? = mRoot.findViewById(R.id.select_range)
		select?.setOnClickListener {
			val builder = MaterialDatePicker.Builder.dateRangePicker()
			val picker = builder.build()
			picker.show(activity?.supportFragmentManager!!, picker.toString())
			picker.addOnNegativeButtonClickListener {
			}
			picker.addOnPositiveButtonClickListener {
				mPlanningPresenter.addPlannedDates(it?.first ?: -1, it?.second ?: -1)
			}
		}
		mSubmitButton = mRoot.findViewById(R.id.submit_plan)
		mLayoutStart = mRoot.findViewById(R.id.chosen_range_begin)
		mTextViewStart = mRoot.findViewById(R.id.plans_starting)
		mLayoutEnd = mRoot.findViewById(R.id.chosen_range_end)
		mTextViewEnd = mRoot.findViewById(R.id.plans_ending)

		addBottomSheetCallback {
			onSlide { _, slideOffset ->
				val alphaTemp = (slideOffset - percent) * (1f / (1f - percent))
				mAlphaCancelButton = if (alphaTemp >= 0) {
					alphaTemp
				} else {
					0f
				}
			}
		}
		mSubmitButton.setOnClickListener {
			val amount = mRoot.findViewById<TextInputEditText>(R.id.plans_textInputEditText)
			mPlanningPresenter.submit(amount.text.toString().toInt())
		}


		return mRoot
	}

	override fun showDates(start: String, end: String) {
		mLayoutStart.visibility = View.VISIBLE
		mTextViewStart.text = start
		mLayoutEnd.visibility = View.VISIBLE
		mTextViewEnd.text = end

	}

	override fun planSuccess() {
		dismissWithBehavior()
	}

	override fun onAttach(context: Context) {
		AndroidSupportInjection.inject(this)
		super.onAttach(context)
		mPlanningPresenter.attachView(this)
	}

	override fun onDestroy() {
		super.onDestroy()
		mPlanningPresenter.detachView()
	}

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
	}

	companion object {
		const val TAG = "PLANNING FRAGMENT"
		fun newInstance() = AddingPlanningFragment()
	}
}
