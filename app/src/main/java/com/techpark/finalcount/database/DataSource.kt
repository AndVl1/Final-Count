package com.techpark.finalcount.database

import android.content.Context
import com.techpark.finalcount.database.room.PurchaseDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSource @Inject constructor(ctx: Context) {
    @InternalCoroutinesApi
    val database = PurchaseDatabase.getInstance(ctx)
}