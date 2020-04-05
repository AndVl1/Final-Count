package com.techpark.finalcount.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "purchases")
data class Purchase (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    /** Always set 0 so that it will generate unique id */
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "cost")
    val cost: Int,
    @ColumnInfo(name = "currency")
    val currency: Int,
    @ColumnInfo(name = "date")
    val date: Long
)
