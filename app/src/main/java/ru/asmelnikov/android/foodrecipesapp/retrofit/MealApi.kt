package ru.asmelnikov.android.foodrecipesapp.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.asmelnikov.android.foodrecipesapp.models.MealList

interface MealApi {

    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php?")
    fun getMealById(@Query("i") id: String): Call<MealList>
}