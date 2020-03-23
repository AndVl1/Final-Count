package com.techpark.finalcount.auth.presenters

import android.content.Intent
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.techpark.finalcount.auth.views.AuthView
import com.techpark.finalcount.base.BasePresenter

interface AuthPresenter: BasePresenter<AuthView> {
    fun authenticateEmail(login: String, password: String, isLogin: Boolean)
    fun handleGoogleAuth(data: Intent?)
    fun authGoogle(acct: GoogleSignInAccount)
    fun authGithub()
    fun authFacebook(token: AccessToken)
    fun checkLogin()
}