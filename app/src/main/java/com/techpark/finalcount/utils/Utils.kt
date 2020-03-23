package com.techpark.finalcount.utils

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.widget.Toast


class Utils<Data> {
    fun setIntentExtra(context: Context, destination: Class<*>?,
                       key: String?, data: Data): Intent {
        val intent = Intent(context, destination)
        intent.putExtra(key, data as Parcelable)
        context.startActivity(intent)
        return intent
    }

    companion object {
        fun setIntent(context: Context, destination: Class<*>?): Intent {
            val intent = Intent(context, destination)
            context.startActivity(intent)
            return intent
        }

        fun showMessage(context: Context?, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}