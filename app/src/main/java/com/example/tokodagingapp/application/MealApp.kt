package com.example.tokodagingapp.application

import android.app.Application
import com.example.tokodagingapp.repository.MealRepository

class MealApp: Application() {
    val database by lazy { MealDatabase.getDatabase(this) }
    val repository by lazy { MealRepository(database.mealDao()) }
}