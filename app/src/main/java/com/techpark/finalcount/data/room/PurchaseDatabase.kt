package com.techpark.finalcount.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.techpark.finalcount.data.room.model.Purchase

@Database(entities = [Purchase::class], version = 3)
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
				.addMigrations(migration1to2)
				.addMigrations(migration2to3)
				.build()

//		@JvmField
//		val MIGRATION_1_2 = Migr

		private val migration1to2 = object : Migration(1, 2) {
			override fun migrate(database: SupportSQLiteDatabase) {
				database.execSQL("CREATE TABLE `p_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `cost` INTEGER NOT NULL, `date` INTEGER NOT NULL)")
				database.execSQL("INSERT INTO p_new (`id`, `name`, `cost`, `date`) SELECT `id`, `name`, `cost`, `date` FROM purchases")
				database.execSQL("DROP TABLE `purchases`")
				database.execSQL("ALTER TABLE `p_new` RENAME TO `purchases`")
			}
		}

		private val migration2to3 = object : Migration(2, 3) {
			override fun migrate(database: SupportSQLiteDatabase) {
				database.execSQL("CREATE TABLE `p_new` (`name` TEXT NOT NULL, `cost` INTEGER NOT NULL, `date` INTEGER PRIMARY KEY NOT NULL)")
				database.execSQL("INSERT INTO p_new (`name`, `cost`, `date`) SELECT `name`, `cost`, `date` FROM purchases")
				database.execSQL("DROP TABLE `purchases`")
				database.execSQL("ALTER TABLE `p_new` RENAME TO `purchases`")
			}

		}
	}
}