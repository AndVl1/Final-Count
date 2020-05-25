package com.techpark.finalcount.data

import android.content.Context
import com.techpark.finalcount.data.room.PlanningDatabase
import com.techpark.finalcount.data.room.PurchaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSource @Inject constructor(ctx: Context) {
	val purchaseDatabase = PurchaseDatabase.getInstance(ctx)
	val planningDatabase = PlanningDatabase.getInstance(ctx)
}