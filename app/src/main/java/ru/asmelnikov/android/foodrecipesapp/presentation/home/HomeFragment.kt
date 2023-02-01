package ru.asmelnikov.android.foodrecipesapp.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.asmelnikov.android.foodrecipesapp.R
import ru.asmelnikov.android.foodrecipesapp.adapters.CategoriesAdapter
import ru.asmelnikov.android.foodrecipesapp.adapters.PopularMealsAdapter
import ru.asmelnikov.android.foodrecipesapp.databinding.FragmentHomeBinding
import ru.asmelnikov.android.foodrecipesapp.models.Meal
import ru.asmelnikov.android.foodrecipesapp.models.MealsByCategory
import ru.asmelnikov.android.foodrecipesapp.utils.loadImage
import kotlin.math.abs
import kotlin.math.pow

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var randomMeal: Meal
    private lateinit var popularAdapter: PopularMealsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

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
        homeViewModel.getCategories()
        observeCategoriesLiveData()
        binding?.recyclerPopularMeal?.addOnScrollListener(CarouselScroller())
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

    private fun observeCategoriesLiveData() {
        homeViewModel.observeCategoriesLiveData()
            .observe(viewLifecycleOwner) { categories ->
                categoriesAdapter.differ
                    .submitList(categories)
            }
    }


    private fun observePopularMeal() {
        homeViewModel.observePopularLiveData()
            .observe(
                viewLifecycleOwner
            ) { mealList ->
                popularAdapter.differ
                    .submitList(mealList as ArrayList<MealsByCategory>)

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

    private class CarouselScroller : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val centerX = (recyclerView.left + recyclerView.right)
            for (i in 0 until recyclerView.childCount) {
                val child = recyclerView.getChildAt(i)
                val childCenterX = (child.left + child.right)
                val childOffset = abs(centerX - childCenterX) / centerX.toFloat()
                val factor = 0.6.pow(childOffset.toDouble()).toFloat()
                child.scaleX = factor
                child.scaleY = factor
            }
        }
    }

    private fun initAdapter() {
        popularAdapter = PopularMealsAdapter()
        binding?.recyclerPopularMeal?.apply {
            adapter = popularAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
        categoriesAdapter = CategoriesAdapter()
        binding?.recyclerCategories?.apply {
            adapter = categoriesAdapter
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        }
    }
}