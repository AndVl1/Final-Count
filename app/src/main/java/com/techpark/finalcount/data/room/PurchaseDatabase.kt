package com.techpark.finalcount.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.techpark.finalcount.data.room.model.Purchase

@Database(entities = [Purchase::class], version = 1)
abstract class PurchaseDatabase: RoomDatabase() {
	abstract fun purchaseDao(): PurchaseDao

	companion object {
		@Volatile
		private var INSTANCE: PurchaseDatabase? = null


		fun getInstance(context: Context): PurchaseDatabase =
			INSTANCE ?: synchronized(this) {
				INSTANCE ?: bindDatabase(context).also { INSTANCE = it }
			}

		private fun bindDatabase(context: Context) =
			Room.databaseBuilder(context.applicationContext, PurchaseDatabase::class.java, "purchase-sample.db")
				.build()
	}
}