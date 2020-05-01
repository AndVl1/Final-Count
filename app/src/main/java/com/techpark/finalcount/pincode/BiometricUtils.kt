package com.techpark.finalcount.pincode

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat


class BiometricUtils {
    companion object {
        fun isBiometricPromptEnabled(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

        fun isSdkVersionSupported(): Boolean = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

        fun isHardwareSupported(context: Context): Boolean {
            val fingerprintManager =
                FingerprintManagerCompat.from(context)
            return fingerprintManager.isHardwareDetected
        }

        fun isFingerprintAvailable(context: Context): Boolean {
            val fingerprintManager = FingerprintManagerCompat.from(context)
            return fingerprintManager.hasEnrolledFingerprints()
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun isPermissionGranted(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) ==
                    PackageManager.PERMISSION_GRANTED
        }
    }


    /** https://proandroiddev.com/5-steps-to-implement-biometric-authentication-in-android-dbeb825aeee8 */

}