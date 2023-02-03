package ru.asmelnikov.android.foodrecipesapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.asmelnikov.android.foodrecipesapp.models.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM mealInformation")
    fun getAllMeals(): LiveData<List<Meal>>

}