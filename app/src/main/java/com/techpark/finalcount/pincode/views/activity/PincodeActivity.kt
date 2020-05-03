package com.techpark.finalcount.pincode.views.activity

import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.techpark.finalcount.R
import com.techpark.finalcount.base.BaseActivity
import com.techpark.finalcount.databinding.ActivityPincodeBinding
import com.techpark.finalcount.main.MainActivityDebug
import com.techpark.finalcount.pincode.BiometricUtils
import com.techpark.finalcount.pincode.presenter.PincodeAddingPresenterImpl
import com.techpark.finalcount.pincode.presenter.PincodePresenter
import com.techpark.finalcount.pincode.presenter.PincodePresenterImpl
import com.techpark.finalcount.pincode.views.PincodeView
import java.util.concurrent.Executor


class PincodeActivity : BaseActivity(), PincodeView {
    private lateinit var mPincodeBinding: ActivityPincodeBinding
    private lateinit var mPincodePresenter: PincodePresenter
    private val circleBorderGreen = R.drawable.circle_border_green
    private val circleBorderRed = R.drawable.circle_border_red
    private val circleEntered = R.drawable.circle_entered

    /** Biometric */
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPincodeBinding = ActivityPincodeBinding.inflate(layoutInflater)
        setContentView(mPincodeBinding.root)

        mSharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
//        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
//        val sharedPrefs = SharedPreferences.create(
//            "PasswordPref",
//            masterKeyAlias,
//            applicationContext,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        ) // TODO find encrypted prefs impl for api 21
        val intent = intent
        mPincodePresenter = if (intent.getBooleanExtra("login", true)) {
            PincodePresenterImpl(getSharedPreferences("MyPref", Context.MODE_PRIVATE))
        } else
            PincodeAddingPresenterImpl(getSharedPreferences("MyPref", Context.MODE_PRIVATE))

        mPincodePresenter.attachView(this)

        if (!BiometricUtils.isSdkVersionSupported()
            || !mPincodePresenter.isLogin()
            || !mSharedPreferences.getBoolean("HAS_SCANNER", false)
        ) {
            mPincodeBinding.pinFinger.visibility = View.INVISIBLE
        }

        /** Biometric*/
        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()
                    if (!mPincodePresenter.isLogin()) {
                        mPincodePresenter.handleScanner(true)
                        onBackPressed()
                    } else {
                        mPincodePresenter.handleScanner(true)
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.use_biometric))
            .setSubtitle(
                if (mPincodePresenter.isLogin())
                    getString(R.string.use_biometric_login)
                else
                    getString(R.string.use_biometric_add)
            )
            .setNegativeButtonText(getString(R.string.cancel))
            .build()

        mPincodeBinding.pinFinger.setOnClickListener {
            displayBiometricPrompt()
        }

        if (mPincodePresenter.isLogin()) {
            displayBiometricPrompt()
        }


        /** Click listeners*/
        KeyboardHandler()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPincodePresenter.detachView()
    }

    override fun addInput(position: Int) {
        Log.d(TAG, "add $position")
        when (position) {
            1 -> imageViewAnimatedChange(mPincodeBinding.imageviewCircle1, circleEntered)
            2 -> imageViewAnimatedChange(mPincodeBinding.imageviewCircle2, circleEntered)
            3 -> imageViewAnimatedChange(mPincodeBinding.imageviewCircle3, circleEntered)
            4 -> imageViewAnimatedChange(mPincodeBinding.imageviewCircle4, circleEntered)
        }
    }

    private fun imageViewAnimatedChange(v: ImageView, res: Int) {
        val animOut: Animation =
            AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out)
        val animIn: Animation =
            AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_in)
        animOut.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                v.setImageResource(res)
                animIn.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationRepeat(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {}
                })
                v.startAnimation(animIn)
            }
        })
        v.startAnimation(animOut)
    }

    override fun pinSuccess() {
        Toast.makeText(applicationContext, "success", Toast.LENGTH_SHORT).show()
        if (mPincodePresenter.isLogin()) {
            startActivity(Intent(applicationContext, MainActivityDebug::class.java))
            finish()
            return
        } else {
            if (BiometricUtils.isSdkVersionSupported()) {
                if (BiometricUtils.isBiometricPromptEnabled()) {
                    ScannerDialog().show(supportFragmentManager, "dialog")
                }
            } else {
                onBackPressed()
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.P)
    private fun displayBiometricPrompt() {
        Log.d(TAG, "prompt")
        biometricPrompt.authenticate(promptInfo)
    }

    override fun showMessage(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun clear() {
        if (!mPincodePresenter.isLogin()) {
            mPincodeBinding.pinMainText.text = getString(R.string.repeat_pin)
        }
        cancel(circleBorderGreen)
    }

    private fun cancel(mode: Int) {
        imageViewAnimatedChange(mPincodeBinding.imageviewCircle1, mode)
        imageViewAnimatedChange(mPincodeBinding.imageviewCircle2, mode)
        imageViewAnimatedChange(mPincodeBinding.imageviewCircle3, mode)
        imageViewAnimatedChange(mPincodeBinding.imageviewCircle4, mode)
    }

    override fun pinFailed() {
        mPincodeBinding.pinMainText.text = getString(R.string.enter_pin)
        cancel(circleBorderRed)
    }

    inner class KeyboardHandler {
        init {
            mPincodeBinding.pinOne.setOnClickListener {
                mPincodePresenter.addNumber("1")
            }
            mPincodeBinding.pinTwo.setOnClickListener {
                mPincodePresenter.addNumber("2")
            }
            mPincodeBinding.pinThree.setOnClickListener {
                mPincodePresenter.addNumber("3")
            }
            mPincodeBinding.pinFour.setOnClickListener {
                mPincodePresenter.addNumber("4")
            }
            mPincodeBinding.pinFive.setOnClickListener {
                mPincodePresenter.addNumber("5")
            }
            mPincodeBinding.pinSix.setOnClickListener {
                mPincodePresenter.addNumber("6")
            }
            mPincodeBinding.pinSeven.setOnClickListener {
                mPincodePresenter.addNumber("7")
            }
            mPincodeBinding.pinEight.setOnClickListener {
                mPincodePresenter.addNumber("7")
            }
            mPincodeBinding.pinNine.setOnClickListener {
                mPincodePresenter.addNumber("9")
            }
            mPincodeBinding.pinZero.setOnClickListener {
                mPincodePresenter.addNumber("0")
            }
            mPincodeBinding.pinFinger.setOnClickListener {
                displayBiometricPrompt()
            }
            mPincodeBinding.pinCancel.setOnClickListener {
                mPincodePresenter.clear()
            }
        }
    }

    companion object {
        const val TAG = "PINCODE ACTIVITY"
    }

    fun dialogYesClicked() {
        displayBiometricPrompt()
    }
}

class ScannerDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.scanner_ask)
            .setPositiveButton(R.string.yes) { _, _ ->
                (activity as PincodeActivity).dialogYesClicked()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }

        return builder.create()
    }
}
