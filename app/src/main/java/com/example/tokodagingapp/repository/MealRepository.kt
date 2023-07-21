package com.example.tokodagingapp.repository

import com.example.tokodagingapp.dao.MealDao
import com.example.tokodagingapp.model.Meal
import kotlinx.coroutines.flow.Flow

class MealRepository(private val mealDao: MealDao) {
    val allMeals: Flow<List<Meal>> = mealDao.getAllMeal()
    suspend fun insertMeal(meal: Meal){
        mealDao.insertMeal(meal)
    }
    suspend fun deleteMeal(meal: Meal){
        mealDao.deleteMeal(meal)
    }
    suspend fun updateMeal(meal: Meal){
        mealDao.updateMeal(meal)
    }
}