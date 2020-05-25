package com.techpark.finalcount.data.room

import androidx.paging.DataSource
import androidx.room.*
import com.techpark.finalcount.data.room.model.Planning

@Dao
interface PlanningDao {
	@Query("SELECT * FROM plans ORDER BY id")
	/*suspend ?*/fun loadAllAsDataSource(): DataSource.Factory<Int, Planning>

	@Query("SELECT * FROM plans ORDER BY `begin`")
	suspend fun loadAll(): List<Planning>

	@Query("SELECT * FROM plans WHERE :date BETWEEN `begin` AND `end`")
	suspend fun loadAllWhereHasDate(date: Long): List<Planning>

	@Query("SELECT * FROM plans WHERE id = :id")
	suspend fun getById(id: Long): Planning

	@Query("DELETE FROM plans")
	suspend fun clear()

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(plan: Planning)

	@Update
	suspend fun update(plan: Planning)

	@Delete
	suspend fun delete(plan: Planning)

	@Delete
	suspend fun delete(plans: List<Planning>)
}
