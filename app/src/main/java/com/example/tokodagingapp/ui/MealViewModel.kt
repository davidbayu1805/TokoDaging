package com.example.tokodagingapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tokodagingapp.model.Meal
import com.example.tokodagingapp.repository.MealRepository
import kotlinx.coroutines.launch

class MealViewModel(private val repository: MealRepository): ViewModel() {
    val allMeals: LiveData<List<Meal>> = repository.allMeals.asLiveData()

    fun insert(meal: Meal) = viewModelScope.launch {
        repository.insertMeal(meal)
    }

    fun delete(meal: Meal) = viewModelScope.launch {
        repository.deleteMeal(meal)
    }
    fun update(meal: Meal) = viewModelScope.launch {
        repository.updateMeal(meal)
    }
}

class MealViewModelFactory(private val repository: MealRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((MealViewModel::class.java))) {
            return MealViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown VieModel Class")
    }
}