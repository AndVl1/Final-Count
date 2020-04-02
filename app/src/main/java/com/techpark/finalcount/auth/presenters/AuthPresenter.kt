package com.techpark.finalcount.auth.presenters

import android.app.Activity
import android.content.Intent
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.techpark.finalcount.auth.views.AuthView
import com.techpark.finalcount.base.BasePresenter

interface AuthPresenter: BasePresenter<AuthView> {
    suspend fun authenticateEmail(login: String, password: String, isLogin: Boolean)
    suspend fun handleGoogleAuth(data: Intent?)
    suspend fun authGoogle(acct: GoogleSignInAccount): AuthResult?
    suspend fun authGithub(activity: Activity)
    suspend fun authFacebook(token: AccessToken)
    suspend fun handleFacebook(token: AccessToken): AuthResult?
    fun checkLogin()
}