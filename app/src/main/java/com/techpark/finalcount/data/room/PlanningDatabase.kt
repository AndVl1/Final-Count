package com.techpark.finalcount.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.techpark.finalcount.data.room.model.Planning

@Database(entities = [Planning::class], version = 1)
abstract class PlanningDatabase: RoomDatabase() {
	abstract fun planningDao(): PlanningDao

	companion object {
		@Volatile
		private var INSTANCE: PlanningDatabase? = null


		fun getInstance(context: Context): PlanningDatabase =
			INSTANCE ?: synchronized(this) {
				INSTANCE ?: bindDatabase(context).also { INSTANCE = it }
			}

		private fun bindDatabase(context: Context) =
			Room.databaseBuilder(context.applicationContext, PlanningDatabase::class.java, "planning.db")
				.build()
	}
}