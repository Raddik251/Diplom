package ru.neotology.nerecipe.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.neotology.nerecipe.R
import ru.neotology.nerecipe.databinding.RecipeContentFragmentBinding

class RecipeContentFragment : Fragment() {

    val args by navArgs<RecipeContentFragmentArgs>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        binding.categoriesMenu.text = args.category
        binding.editTitle.requestFocus()
        binding.ok.setOnClickListener {
            onOkButtonClicked(binding)
        }

        val categoriesMenu by lazy {
            PopupMenu(layoutInflater.context, binding.categoriesMenu).apply {
                inflate(R.menu.categories)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.catEuropea -> {
                            binding.categoriesMenu.text = "European"
                            true
                        }
                        R.id.catAsian -> {
                            binding.categoriesMenu.text = "Asian"
                            true
                        }
                        R.id.catPanAsian -> {
                            binding.categoriesMenu.text = "Pan-Asian"
                            true
                        }
                        R.id.catEastern -> {
                            binding.categoriesMenu.text = "Eastern"
                            true
                        }
                        R.id.catAmerican -> {
                            binding.categoriesMenu.text = "American"
                            true
                        }
                        R.id.catRussian -> {
                            binding.categoriesMenu.text = "Russian"
                            true
                        }
                        R.id.catMediterranean -> {
                            binding.categoriesMenu.text = "Mediterranean"
                            true
                        }
                        else -> false
                    }
                }
            }
        }
        binding.categoriesMenu.setOnClickListener { categoriesMenu.show() }

    }.root

    private fun onOkButtonClicked(binding: RecipeContentFragmentBinding) {
        val recipeArray = ArrayList<String?>()
        recipeArray.add(binding.editTitle.text.toString())
        recipeArray.add(binding.categoriesMenu.text.toString())
        recipeArray.add(binding.editContent.text.toString())
        if (recipeArray.isNotEmpty()) {
            val resultBundle = Bundle(1)
            resultBundle.putStringArrayList(RESULT_KEY, recipeArray)
            setFragmentResult(REQUEST_KEY, resultBundle)
        }
        findNavController().navigate(RecipeContentFragmentDirections.toFeedFragment())
    }


    companion object {

        const val REQUEST_KEY = "requestKey"
        const val RESULT_KEY = "RecipeNewContent"

    }
}