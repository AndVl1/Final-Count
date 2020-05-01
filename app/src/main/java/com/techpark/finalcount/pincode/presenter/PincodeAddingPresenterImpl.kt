package com.techpark.finalcount.pincode.presenter

import android.content.SharedPreferences
import android.util.Log
import com.techpark.finalcount.pincode.views.PincodeView

class PincodeAddingPresenterImpl(var sharedPreferences: SharedPreferences): PincodePresenter {
    private var mView: PincodeView? = null
    private var mCurrentInput = StringBuilder()
    private var mFirstInput = StringBuilder()

    override fun addNumber(num: String) {
        Log.d(PincodePresenterImpl.TAG, "$num $mCurrentInput")
        if (mCurrentInput.length < 4) {
            mCurrentInput.append(num)
            mView?.addInput(mCurrentInput.length)
        }
        if (mCurrentInput.length == 4) {
            if (mFirstInput.isEmpty()) {
                Log.d(TAG, mCurrentInput.toString())
                save()
            } else {
                check()
            }
        }
    }

    private fun check() {
        if (mCurrentInput.toString() == mFirstInput.toString()) {
            Log.d(TAG, "$mCurrentInput, $mFirstInput")
            val editor = sharedPreferences.edit()
            editor.putBoolean("HAS_PIN", true)
//            editor.putString("PIN", Base64 .encodeToString(mCurrentInput.toString().toByteArray(), Base64.DEFAULT))
            editor.putString("PIN", mCurrentInput.toString())
            editor.apply()
            mView?.pinSuccess()
        } else {
            mCurrentInput.clear()
            mFirstInput.clear()
            mView?.pinFailed()
            mView?.showMessage("Try again")
        }
    }

    private fun save() {
        mFirstInput.append(mCurrentInput)
        mCurrentInput.clear()
        mView?.clear()
    }

    override fun clear() {
        mCurrentInput.clear()
        mView?.clear()
    }

    override fun handleScanner() {
        TODO("Not yet implemented")
    }

    override fun attachView(view: PincodeView) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun isLogin(): Boolean = false

    companion object {
        const val TAG = "PIN PRESENTER CREATE"
    }
}