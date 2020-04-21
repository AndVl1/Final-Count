package com.techpark.finalcount.database.room

import androidx.room.*
import com.techpark.finalcount.database.model.Purchase

@Dao
interface PurchaseDao {
    @Query("SELECT * FROM purchases")
    suspend fun loadAll(): List<Purchase>

    @Query("SELECT * FROM purchases WHERE id = :id")
    suspend fun getById(id: Long): Purchase

    @Query("SELECT * FROM purchases WHERE date BETWEEN :date1 AND :date2")
    suspend fun getByMonth(date1: Long, date2: Long): List<Purchase>

    @Query("SELECT * FROM purchases WHERE cost > :cost")
    suspend fun getWhereCostsMore(cost: Int): List<Purchase>

    @Query("SELECT * FROM purchases WHERE cost < :cost")
    suspend fun getWhereCostsLess(cost: Int): List<Purchase>

    @Query("DELETE FROM purchases")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(p: Purchase)

    @Update
    suspend fun update(p: Purchase)

    @Delete
    suspend fun delete(p: Purchase)

    @Delete
    suspend fun delete(p: List<Purchase>)


}