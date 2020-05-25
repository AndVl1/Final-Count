package com.techpark.finalcount.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "plans")
data class Planning (
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val id: Long,
	@ColumnInfo(name = "begin")
	val begin: Long,
	@ColumnInfo(name = "end")
	val end: Long,
	@ColumnInfo(name = "planned")
	val planned: Int,
	@ColumnInfo(name = "spent")
	val spent: Int
)
