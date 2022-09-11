package ru.neotology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.neotology.nerecipe.R
import ru.neotology.nerecipe.databinding.RecipeSingleBinding
import ru.neotology.nerecipe.dto.Recipe
import ru.neotology.nerecipe.viewModel.RecipeViewModel

class SingleRecipeFragment : Fragment() {

    private val viewModel by viewModels<RecipeViewModel>()

    private val args by navArgs<SingleRecipeFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigateToRecipeScreenEvent.observe(this) { initialRecipe ->
            val direction = SingleRecipeFragmentDirections.toRecipeContentFragment(
                initialRecipe.title,
                initialRecipe.category,
                initialRecipe.content
            )
            findNavController().navigate(direction)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeSingleBinding.inflate(layoutInflater, container, false).also { binding ->

        val recipe = Recipe(
            id = args.id,
            title = args.title,
            content = args.content,
            author = args.author,
            category = args.category,
            favoriteToMe = args.favoriteToMe
        )

        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            if (recipes.find { it.id == recipe.id } == null) findNavController().navigate(
                SingleRecipeFragmentDirections.toFeedFragment()
            )
            else {

                with(binding) {
                    titleRecipe.text = recipe.title
                    content.text = recipe.content
                    authorRecipe.text = recipe.author
                    categoryRecipe.text = recipe.category

                    favoriteIcon.setOnClickListener { viewModel.onFavoriteClicked(recipe) }
                }

                val popupMenu by lazy {
                    PopupMenu(layoutInflater.context, binding.options).apply {
                        inflate(R.menu.option_recipe)
                        setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.remove -> {
                                    viewModel.onRemoveClicked(recipe)
                                    true
                                }
                                R.id.edit -> {
                                    viewModel.onEditClicked(recipe)
                                    true
                                }
                                else -> false
                            }
                        }
                    }
                }
                binding.options.setOnClickListener { popupMenu.show() }
            }
        }
    }.root

}