package com.techpark.finalcount.main.views

import com.techpark.finalcount.base.BaseView
import java.io.File

interface MainView: BaseView {
	fun showMsg(string: String)
	fun toAuthActivity()
	fun startShareIntent(file: File)
}