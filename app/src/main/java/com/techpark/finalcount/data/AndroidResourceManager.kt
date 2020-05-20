package com.techpark.finalcount.data

import android.content.Context
import com.techpark.finalcount.R
import javax.inject.Inject

class AndroidResourceManager @Inject constructor(var context: Context) : ResourceManager {


	override fun getStringInvalidError() = context.getString(R.string.invalid)
}