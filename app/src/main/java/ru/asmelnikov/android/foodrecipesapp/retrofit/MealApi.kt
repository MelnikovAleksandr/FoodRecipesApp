package ru.asmelnikov.android.foodrecipesapp.retrofit

import retrofit2.Call
import retrofit2.http.GET
import ru.asmelnikov.android.foodrecipesapp.pojo.MealList

interface MealApi {

    @GET("random.php")
    fun getRandomMeal(): Call<MealList>
}