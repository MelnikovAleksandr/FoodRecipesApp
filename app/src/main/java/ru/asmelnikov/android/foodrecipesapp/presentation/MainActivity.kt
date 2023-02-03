package ru.asmelnikov.android.foodrecipesapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.asmelnikov.android.foodrecipesapp.R
import ru.asmelnikov.android.foodrecipesapp.databinding.ActivityMainBinding
import ru.asmelnikov.android.foodrecipesapp.db.MealDatabase
import ru.asmelnikov.android.foodrecipesapp.presentation.home.HomeViewModel
import ru.asmelnikov.android.foodrecipesapp.presentation.home.HomeViewModelFactory

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding
    val viewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelProviderFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this, homeViewModelProviderFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val navController = Navigation.findNavController(this, R.id.nav_fragment)

        NavigationUI.setupWithNavController(bottomNavigation, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment -> binding?.bottomNav?.visibility = View.GONE
                R.id.categoryMealsFragment -> binding?.bottomNav?.visibility = View.GONE
                else -> binding?.bottomNav?.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}