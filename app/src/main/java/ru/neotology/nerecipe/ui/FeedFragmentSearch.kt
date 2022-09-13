package ru.neotology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.neotology.nerecipe.R
import ru.neotology.nerecipe.adapter.RecipeAdapter
import ru.neotology.nerecipe.databinding.FeedFragmentBinding
import ru.neotology.nerecipe.databinding.FeedFragmentFavoriteBinding
import ru.neotology.nerecipe.databinding.FeedFragmentSearchBinding
import ru.neotology.nerecipe.viewModel.RecipeViewModel

class FeedFragmentSearch : Fragment() {

    private val viewModel by viewModels<RecipeViewModel>()

    private val args by navArgs<FeedFragmentSearchArgs>()

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
            val direction = FeedFragmentSearchDirections.toRecipeContentFragment(
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
            val direction = FeedFragmentSearchDirections.toSingleRecipeFragment(
                singleRecipe.id,
                singleRecipe.title,
                singleRecipe.content,
                singleRecipe.author,
                singleRecipe.category,
                singleRecipe.favoriteToMe
            )
            findNavController().navigate(direction)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentSearchBinding.inflate(layoutInflater, container, false).also { binding ->



        val adapter = RecipeAdapter(viewModel)
        binding.recipeRecyclerViewFSearch.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes.filter { it.title.contains(args.partOfTitle.toString()) })
        }

        binding.fabHomeFromSearch.setOnClickListener {
            findNavController().navigate(FeedFragmentSearchDirections.toFeedFragment())
        }

    }.root

}