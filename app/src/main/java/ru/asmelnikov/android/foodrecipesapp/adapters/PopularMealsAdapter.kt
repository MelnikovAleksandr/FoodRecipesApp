package ru.asmelnikov.android.foodrecipesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.asmelnikov.android.foodrecipesapp.databinding.PopularItemsBinding
import ru.asmelnikov.android.foodrecipesapp.models.CategoryMeals
import ru.asmelnikov.android.foodrecipesapp.utils.loadImage


class PopularMealsAdapter :
    RecyclerView.Adapter<PopularMealsAdapter.PopularMealViewHolder>() {

    private lateinit var binding: PopularItemsBinding

    inner class PopularMealViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun set(item: CategoryMeals) {
            binding.apply {
                imgPopularMealItem.loadImage(item.strMealThumb)
            }
        }
    }

    private val callBack = object : DiffUtil.ItemCallback<CategoryMeals>() {
        override fun areItemsTheSame(oldItem: CategoryMeals, newItem: CategoryMeals): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: CategoryMeals, newItem: CategoryMeals): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = PopularItemsBinding.inflate(inflater, parent, false)
        return PopularMealViewHolder()
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        holder.set(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}