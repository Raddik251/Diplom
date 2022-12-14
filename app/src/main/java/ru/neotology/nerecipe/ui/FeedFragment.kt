package ru.neotology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.neotology.nerecipe.R
import ru.neotology.nerecipe.adapter.RecipeAdapter
import ru.neotology.nerecipe.databinding.FeedFragmentBinding
import ru.neotology.nerecipe.viewModel.RecipeViewModel

class FeedFragment : Fragment() {

    private val viewModel by viewModels<RecipeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(
            requestKey = RecipeContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != RecipeContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newRecipeArray = bundle.getStringArrayList(
                RecipeContentFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newRecipeArray[0],newRecipeArray[1],newRecipeArray[2])
        }

        viewModel.navigateToRecipeScreenEvent.observe(this) { initialRecipe ->
            val direction = FeedFragmentDirections.toRecipeContentFragment(
                initialRecipe.id,
                initialRecipe.title,
                initialRecipe.content,
                initialRecipe.author,
                initialRecipe.category,
                initialRecipe.favoriteToMe
            )
            findNavController().navigate(direction)
        }

        viewModel.navigateToRecipeSingleScreenEvent.observe(this) { singleRecipe ->
            val direction = FeedFragmentDirections.toSingleRecipeFragment(
                singleRecipe.id,
                singleRecipe.title,
                singleRecipe.content,
                singleRecipe.author,
                singleRecipe.category,
                singleRecipe.favoriteToMe
            )
            findNavController().navigate(direction)
        }

        viewModel.navigateToRecipeSearchEvent.observe(this) { partOfTitle ->
            val direction = FeedFragmentDirections.toFeedFragmentSearch(partOfTitle)
            findNavController().navigate(direction)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = RecipeAdapter(viewModel)
        binding.recipeRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes)
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }

        binding.fabFavorite.setOnClickListener {
            findNavController().navigate(FeedFragmentDirections.toFeedFragmentFavorite())
        }

        binding.fabSearch.setOnClickListener {
            viewModel.onSearchClicked(binding.textInputLayoutSearch.editText?.text.toString())
        }
    }.root

}