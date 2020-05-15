package com.techpark.finalcount.data.room

import androidx.room.*
import com.techpark.finalcount.data.room.model.Purchase
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.future.future
//import java.util.concurrent.CompletableFuture

@Dao
interface PurchaseDao {
	@Query("SELECT * FROM purchases ORDER BY date")
	suspend fun loadAll(): List<Purchase>

//    //https://stackoverflow.com/questions/52869672/call-kotlin-suspend-function-in-java-class
//    @Query("SELECT * FROM purchases ORDER BY date")
//    fun loadAllAsync(): CompletableFuture<List<Purchase>> =
//        GlobalScope.future { loadAll() }

	@Query("SELECT * FROM purchases WHERE id = :id")
	suspend fun getById(id: Long): Purchase

	@Query("SELECT * FROM purchases WHERE date BETWEEN :date1 AND :date2 ORDER BY date")
	suspend fun getByMonth(date1: Long, date2: Long): List<Purchase>

	@Query("SELECT * FROM purchases WHERE cost > :cost")
	suspend fun getWhereCostsMore(cost: Int): List<Purchase>

	@Query("SELECT * FROM purchases WHERE cost < :cost")
	suspend fun getWhereCostsLess(cost: Int): List<Purchase>

	@Query("DELETE FROM purchases")
	suspend fun clear()

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(purchase: Purchase)

	@Update
	suspend fun update(purchase: Purchase)

	@Delete
	suspend fun delete(purchase: Purchase)

	@Delete
	suspend fun delete(purchases: List<Purchase>)


}
