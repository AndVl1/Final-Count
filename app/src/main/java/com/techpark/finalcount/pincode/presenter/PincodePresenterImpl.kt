package com.techpark.finalcount.pincode.presenter

import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import com.techpark.finalcount.pincode.views.PincodeView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class PincodePresenterImpl(sharedPrefs: SharedPreferences): PincodePresenter {
    var mView : PincodeView? = null
    var mCurrentInput = StringBuilder()
    private val mPin = sharedPrefs.getString("PIN", "1234")
    override fun addNumber(num: String) {
        Log.d(TAG, "$num $mCurrentInput")
        if (mCurrentInput.length < 4) {
            mCurrentInput.append(num)
            mView?.addInput(mCurrentInput.length)
        }
        if (mCurrentInput.length == 4) {
            check()
        }

    }

    private fun check() {
        Log.d(TAG, mPin)
        MainScope().launch {
            delay(500)
            if (mCurrentInput.toString() == mPin) {
                mCurrentInput.clear()
                mView?.pinSuccess()
            } else {
                mCurrentInput.clear()
                mView?.pinFailed()
            }
        }
    }

    override fun clear() {
        mCurrentInput.clear()
        mView?.clear()
    }

    override fun handleScanner() {
        //TODO("Not yet implemented")
    }

    override fun attachView(view: PincodeView) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun isLogin(): Boolean = true

    companion object {
        const val TAG = "PIN PRESENTER"
    }
}