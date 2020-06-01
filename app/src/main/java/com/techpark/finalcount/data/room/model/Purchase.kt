package com.techpark.finalcount.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "purchases")
data class Purchase (
	@ColumnInfo(name = "name")
	val name: String,
	@ColumnInfo(name = "cost")
	val cost: Int,
	@PrimaryKey(autoGenerate = false)
	@ColumnInfo(name = "date")
	val date: Long
)
