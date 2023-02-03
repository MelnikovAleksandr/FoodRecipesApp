package ru.asmelnikov.android.foodrecipesapp.presentation.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import ru.asmelnikov.android.foodrecipesapp.R
import ru.asmelnikov.android.foodrecipesapp.databinding.FragmentDetailsBinding
import ru.asmelnikov.android.foodrecipesapp.db.MealDatabase
import ru.asmelnikov.android.foodrecipesapp.models.Meal
import ru.asmelnikov.android.foodrecipesapp.utils.loadImage

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding
    private val bundleArgs: DetailsFragmentArgs by navArgs()
    private lateinit var dtMeal: Meal
    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mealDatabase = MealDatabase.getInstance(requireContext())
        val viewModelFactory = DetailsViewModelFactory(mealDatabase)
        viewModel = ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]

        showLoading()
        val mealArgs = bundleArgs.mealId
        viewModel.getMealById(mealArgs)
        viewModel.observeMealDetail().observe(
            viewLifecycleOwner
        ) { meals ->
            setTextsInViews(meals!![0])
            stopLoading()
        }

    }

    private fun setTextsInViews(meal: Meal) {
        this.dtMeal = meal
        binding?.collapsingToolBar?.title = meal.strMeal
        binding?.content?.text = meal.strInstructions
        binding?.tvCategories?.text = getString(R.string.categories2, meal.strCategory)
        binding?.tvArea?.text = getString(R.string.area, meal.strArea)
        meal.strMealThumb.let {
            binding?.imgMealDetails?.loadImage(meal.strMealThumb)
        }
        binding?.btnFavoritesAdd?.setOnClickListener {
            meal.let {
                viewModel.insertMeal(it)
                Toast.makeText(requireContext(), "Meal saved!", Toast.LENGTH_LONG).show()
            }
        }
        onYouTubeImageClick(meal.strYoutube!!)

    }

    private fun onYouTubeImageClick(url: String) {
        binding?.imgYoutube?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun showLoading() {
        binding?.collapsingToolBar?.visibility = View.INVISIBLE
        binding?.tvCategories?.visibility = View.INVISIBLE
        binding?.tvArea?.visibility = View.INVISIBLE
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.btnFavoritesAdd?.visibility = View.GONE
        binding?.imgYoutube?.visibility = View.INVISIBLE
    }


    private fun stopLoading() {
        binding?.collapsingToolBar?.visibility = View.VISIBLE
        binding?.tvCategories?.visibility = View.VISIBLE
        binding?.tvArea?.visibility = View.VISIBLE
        binding?.progressBar?.visibility = View.INVISIBLE
        binding?.btnFavoritesAdd?.visibility = View.VISIBLE
        binding?.imgYoutube?.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}