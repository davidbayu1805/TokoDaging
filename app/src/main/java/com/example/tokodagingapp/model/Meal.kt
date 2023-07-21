package com.example.tokodagingapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "meal_table")
data class Meal (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val count: String,
    val address: String,
    val latitude: Double?,
    val longitude: Double?
    ) : Parcelable