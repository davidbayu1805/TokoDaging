package com.example.tokodagingapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tokodagingapp.model.Meal
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * FROM 'meal_table' ORDER BY name ASC")
    fun getAllMeal(): Flow<List<Meal>>
    @Insert
    suspend fun insertMeal(meal: Meal)
    @Delete
    suspend fun deleteMeal(meal: Meal)
    @Update
    fun updateMeal(meal: Meal)
}