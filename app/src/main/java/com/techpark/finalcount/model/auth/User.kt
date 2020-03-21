package com.techpark.finalcount.model.auth

import com.google.firebase.auth.FirebaseAuth

class User {
    lateinit var login: String
    lateinit var password: String
    lateinit var UID: String
    val mAuth = FirebaseAuth.getInstance()
    init{
    }
}