package ru.asmelnikov.android.foodrecipesapp.presentation.favorites

import android.os.Bundle
import android.util.Log
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
import ru.asmelnikov.android.foodrecipesapp.adapters.FavoritesAdapter
import ru.asmelnikov.android.foodrecipesapp.adapters.PopularMealsAdapter
import ru.asmelnikov.android.foodrecipesapp.databinding.FragmentFavoritesBinding
import ru.asmelnikov.android.foodrecipesapp.presentation.MainActivity
import ru.asmelnikov.android.foodrecipesapp.presentation.home.HomeViewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoriteAdapter: FavoritesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        observeFavorites()

        initAdapter()

        favoriteAdapter.setOnItemClickListener {
            val bundle = bundleOf("meal_id" to it)
            findNavController().navigate(R.id.action_favoritesFragment_to_detailsFragment, bundle)
        }


    }

    private fun observeFavorites() {
        viewModel.observeFavoritesLiveData().observe(viewLifecycleOwner) { meals ->
            favoriteAdapter.differ.submitList(meals)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        favoriteAdapter = FavoritesAdapter()
        binding?.recyclerFavorites?.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}