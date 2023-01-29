package ru.asmelnikov.android.foodrecipesapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.asmelnikov.android.foodrecipesapp.databinding.FragmentHomeBinding
import ru.asmelnikov.android.foodrecipesapp.pojo.Meal
import ru.asmelnikov.android.foodrecipesapp.pojo.MealList
import ru.asmelnikov.android.foodrecipesapp.retrofit.RetrofitInstance
import ru.asmelnikov.android.foodrecipesapp.utils.loadImage

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    binding.imgRandomMeal.loadImage(randomMeal.strMealThumb)
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HOME_FRAGMENT", t.message.toString())
            }

        })
    }
}