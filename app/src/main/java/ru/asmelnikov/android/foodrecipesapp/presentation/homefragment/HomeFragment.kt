package ru.asmelnikov.android.foodrecipesapp.presentation.homefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import ru.asmelnikov.android.foodrecipesapp.databinding.FragmentHomeBinding
import ru.asmelnikov.android.foodrecipesapp.pojo.Meal
import ru.asmelnikov.android.foodrecipesapp.utils.loadImage

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getRandomMeal()
        observerRandomMeal()
    }

    private fun observerRandomMeal() {
        homeViewModel.observeRandomMealLiveData()
            .observe(
                viewLifecycleOwner
            ) { t -> binding.imgRandomMeal.loadImage(t!!.strMealThumb) }
    }
}