package ru.asmelnikov.android.foodrecipesapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ru.asmelnikov.android.foodrecipesapp.R
import ru.asmelnikov.android.foodrecipesapp.adapters.CategoriesAdapter
import ru.asmelnikov.android.foodrecipesapp.adapters.PopularMealsAdapter
import ru.asmelnikov.android.foodrecipesapp.databinding.FragmentHomeBinding
import ru.asmelnikov.android.foodrecipesapp.models.Meal
import ru.asmelnikov.android.foodrecipesapp.models.MealsByCategory
import ru.asmelnikov.android.foodrecipesapp.presentation.MainActivity
import ru.asmelnikov.android.foodrecipesapp.utils.CarouselScroller
import ru.asmelnikov.android.foodrecipesapp.utils.loadImage

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: HomeViewModel
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

        viewModel = (activity as MainActivity).viewModel

        showLoadingCase()
        viewModel.getRandomMeal()
        observerRandomMeal()
        viewModel.getPopularItems()
        initAdapter()
        observePopularMeal()
        viewModel.getCategories()
        observeCategoriesLiveData()
        binding?.recyclerPopularMeal?.addOnScrollListener(CarouselScroller())
        binding?.imgRandomMeal?.setOnClickListener {
            onClick?.let {
                it(randomMeal.idMeal.toString())
            }
        }
        onRandomMealClick {
            val bundle = bundleOf("meal_id" to it)
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        }
        popularAdapter.setOnItemClickListener {
            val bundle = bundleOf("meal_id" to it)
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        }
        categoriesAdapter.setOnItemClickListener {
            val bundle = bundleOf("category_name" to it)
            findNavController().navigate(R.id.action_homeFragment_to_categoryMealsFragment, bundle)
        }

    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData()
            .observe(viewLifecycleOwner) { categories ->
                categoriesAdapter.differ
                    .submitList(categories)
                cancelLoadingCase()
            }
    }


    private fun observePopularMeal() {
        viewModel.observePopularLiveData()
            .observe(
                viewLifecycleOwner
            ) { mealList ->
                popularAdapter.differ
                    .submitList(mealList as ArrayList<MealsByCategory>)
            }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData()
            .observe(
                viewLifecycleOwner
            ) { meal ->
                binding?.imgRandomMeal?.loadImage(meal!!.strMealThumb)
                this.randomMeal = meal
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
            imgRandomMeal.visibility = View.INVISIBLE
            linearHead.visibility = View.INVISIBLE
            tvLikeToEat.visibility = View.INVISIBLE
            randomMealCard.visibility = View.INVISIBLE
            tvPopularMeal.visibility = View.INVISIBLE
            recyclerPopularMeal.visibility = View.INVISIBLE
            tvCategories.visibility = View.INVISIBLE
            recyclerCategories.visibility = View.INVISIBLE
            loadingGif.visibility = View.VISIBLE
        }
    }

    private fun cancelLoadingCase() {
        binding?.apply {
            imgRandomMeal.visibility = View.VISIBLE
            linearHead.visibility = View.VISIBLE
            tvLikeToEat.visibility = View.VISIBLE
            randomMealCard.visibility = View.VISIBLE
            tvPopularMeal.visibility = View.VISIBLE
            recyclerPopularMeal.visibility = View.VISIBLE
            tvCategories.visibility = View.VISIBLE
            recyclerCategories.visibility = View.VISIBLE
            loadingGif.visibility = View.INVISIBLE
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