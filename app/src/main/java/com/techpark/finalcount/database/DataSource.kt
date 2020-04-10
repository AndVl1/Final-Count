package com.techpark.finalcount.database

import android.content.Context
import com.techpark.finalcount.database.room.PurchaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSource @Inject constructor(ctx: Context) {
    val database = PurchaseDatabase.getInstance(ctx)
}