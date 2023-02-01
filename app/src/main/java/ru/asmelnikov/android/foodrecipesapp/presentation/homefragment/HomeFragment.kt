package ru.asmelnikov.android.foodrecipesapp.presentation.homefragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.asmelnikov.android.foodrecipesapp.R
import ru.asmelnikov.android.foodrecipesapp.adapters.PopularMealsAdapter
import ru.asmelnikov.android.foodrecipesapp.databinding.FragmentHomeBinding
import ru.asmelnikov.android.foodrecipesapp.models.CategoryMeals
import ru.asmelnikov.android.foodrecipesapp.models.Meal
import ru.asmelnikov.android.foodrecipesapp.utils.loadImage

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var randomMeal: Meal
    private lateinit var popularAdapter: PopularMealsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingCase()
        homeViewModel.getRandomMeal()
        observerRandomMeal()
        homeViewModel.getPopularItems()
        initAdapter()
        observePopularMeal()
        binding?.imgRandomMeal?.setOnClickListener {
            onClick?.let {
                it(randomMeal.idMeal.toString())
            }
        }
        popularAdapter.setOnItemClickListener {
            Log.d("HOME_ARGS", it)
            val bundle = bundleOf("meal_id" to it)
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        }
        onRandomMealClick {
            val bundle = bundleOf("meal_id" to it)
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        }
    }


    private fun observePopularMeal() {
        homeViewModel.observePopularLiveData()
            .observe(
                viewLifecycleOwner
            ) { mealList ->
                popularAdapter.differ
                    .submitList(mealList as ArrayList<CategoryMeals>)

            }
    }

    private fun observerRandomMeal() {
        homeViewModel.observeRandomMealLiveData()
            .observe(
                viewLifecycleOwner
            ) { meal ->
                binding?.imgRandomMeal?.loadImage(meal!!.strMealThumb)
                this.randomMeal = meal
                cancelLoadingCase()
            }
    }

    private var onClick: ((String) -> Unit)? = null

    private fun onRandomMealClick(listener: (String) -> Unit) {
        onClick = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoadingCase() {
        binding?.apply {
            linearHead.visibility = View.INVISIBLE
            tvLikeToEat.visibility = View.INVISIBLE
            randomMealCard.visibility = View.INVISIBLE
            recyclerPopularMeal.visibility = View.INVISIBLE
            tvCategories.visibility = View.INVISIBLE
            loadingGif.visibility = View.VISIBLE
        }
    }

    private fun cancelLoadingCase() {
        binding?.apply {
            linearHead.visibility = View.VISIBLE
            tvLikeToEat.visibility = View.VISIBLE
            randomMealCard.visibility = View.VISIBLE
            recyclerPopularMeal.visibility = View.VISIBLE
            tvCategories.visibility = View.VISIBLE
            loadingGif.visibility = View.INVISIBLE
        }
    }

    private fun initAdapter() {
        popularAdapter = PopularMealsAdapter()
        binding?.recyclerPopularMeal?.apply {
            adapter = popularAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}