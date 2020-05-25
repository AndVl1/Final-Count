package com.techpark.finalcount.auth.presenters

import android.app.Activity
import android.content.Intent
import com.facebook.AccessToken
import com.techpark.finalcount.auth.views.AuthView
import com.techpark.finalcount.base.BasePresenter

interface AuthPresenter: BasePresenter<AuthView> {
	fun authenticateEmail(login: String, password: String, isLogin: Boolean)
	fun handleGoogleAuth(data: Intent?)
	fun authGithub(activity: Activity)
	fun authFacebook(token: AccessToken)
	fun checkLogin()
}