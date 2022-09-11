package ru.neotology.nerecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.neotology.nerecipe.R
import ru.neotology.nerecipe.databinding.RecipeBinding
import ru.neotology.nerecipe.dto.Recipe

internal class RecipeAdapter(
    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, RecipeAdapter.ViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: RecipeBinding,
        listener: RecipeInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {
                inflate(R.menu.option_recipe)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(recipe)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(recipe)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.favoriteIcon.setOnClickListener { listener.onFavoriteClicked(recipe) }
            binding.options.setOnClickListener { popupMenu.show()}
            binding.avatar.setOnClickListener { listener.onSingleRecipeShow(recipe) }
            binding.titleRecipe.setOnClickListener { listener.onSingleRecipeShow(recipe) }
            binding.categoryRecipe.setOnClickListener { listener.onSingleRecipeShow(recipe) }
            binding.authorRecipe.setOnClickListener { listener.onSingleRecipeShow(recipe) }
        }

        fun bind(recipe: Recipe) {

            this.recipe = recipe

            with(binding) {
                titleRecipe.text = recipe.title
                categoryRecipe.text = recipe.category
                authorRecipe.text = recipe.author
                favoriteIcon.isChecked = recipe.favoriteToMe
            }
        }
    }
}

private object DiffCallBack : DiffUtil.ItemCallback<Recipe>() {

    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
        oldItem == newItem
}