package ru.asmelnikov.android.foodrecipesapp.presentation.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.asmelnikov.android.foodrecipesapp.models.Meal
import ru.asmelnikov.android.foodrecipesapp.models.MealList
import ru.asmelnikov.android.foodrecipesapp.retrofit.RetrofitInstance

class DetailsViewModel : ViewModel() {

    private val mealDetails = MutableLiveData<List<Meal>>()

    fun getMealById(id: String) {
        RetrofitInstance.api.getMealById(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                mealDetails.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("onFailure get meal by id", t.message.toString())
            }
        })
    }

    fun observeMealDetail(): LiveData<List<Meal>> {
        return mealDetails
    }

}