package com.techpark.finalcount.data

import android.content.Context
import com.techpark.finalcount.data.room.PurchaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSource @Inject constructor(ctx: Context) {
	val database = PurchaseDatabase.getInstance(ctx)
}