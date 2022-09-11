package ru.neotology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.neotology.nerecipe.R
import ru.neotology.nerecipe.databinding.RecipeContentFragmentBinding
import ru.neotology.nerecipe.viewModel.RecipeViewModel

class RecipeContentFragment : Fragment() {

    private val viewModel by viewModels<RecipeViewModel>()

    val args by navArgs<RecipeContentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        binding.editTitle.setText(args.initialTitle)
        binding.options.text = args.initialCategory
        binding.editTitle.setText(args.initialContent)
        binding.editTitle.requestFocus()
        binding.ok.setOnClickListener {
            onOkButtonClicked(binding)
        }

        val popupMenu by lazy {
            PopupMenu(layoutInflater.context, binding.options).apply {
                inflate(R.menu.categories)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.catEuropea -> {
                            binding.options.text = R.string.catEuropean.toString()
                            true
                        }
                        R.id.catAsian -> {
                            binding.options.text = R.string.catAsian.toString()
                            true
                        }
                        R.id.catPanAsian -> {
                            binding.options.text = R.string.catPanAsian.toString()
                            true
                        }
                        R.id.catEastern -> {
                            binding.options.text = R.string.catEastern.toString()
                            true
                        }
                        R.id.catAmerican -> {
                            binding.options.text = R.string.catAmerican.toString()
                            true
                        }
                        R.id.catRussian -> {
                            binding.options.text = R.string.catRussian.toString()
                            true
                        }
                        R.id.catMediterranean -> {
                            binding.options.text = R.string.catMediterranean.toString()
                            true
                        }
                        else -> false
                    }
                }
            }
        }
        binding.options.setOnClickListener { popupMenu.show() }

    }.root

    private fun onOkButtonClicked(binding: RecipeContentFragmentBinding) {
        val recipeArray = ArrayList<String?>()
        recipeArray.add(binding.editTitle.text.toString())
        recipeArray.add(binding.options.text.toString())
        recipeArray.add(binding.editaContent.text.toString())
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