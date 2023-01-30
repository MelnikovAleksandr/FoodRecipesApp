package ru.asmelnikov.android.foodrecipesapp.presentation.homefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.asmelnikov.android.foodrecipesapp.R
import ru.asmelnikov.android.foodrecipesapp.databinding.FragmentHomeBinding
import ru.asmelnikov.android.foodrecipesapp.models.Meal
import ru.asmelnikov.android.foodrecipesapp.utils.loadImage

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var randomMeal: Meal

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getRandomMeal()
        observerRandomMeal()
        binding?.imgRandomMeal?.setOnClickListener {
            onClick?.let {
                it(randomMeal)
            }
        }
        onMovieClick {
            val bundle = bundleOf("meal" to it)
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        }
    }

    private var onClick: ((Meal) -> Unit)? = null

    private fun onMovieClick(listener: (Meal) -> Unit) {
        onClick = listener
    }

    private fun observerRandomMeal() {
        homeViewModel.observeRandomMealLiveData().observe(
            viewLifecycleOwner
        ) { meal ->
            binding?.imgRandomMeal?.loadImage(meal!!.strMealThumb)
            this.randomMeal = meal
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}