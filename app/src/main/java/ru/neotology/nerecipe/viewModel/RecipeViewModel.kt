package ru.neotology.nerecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.neotology.nerecipe.util.SingleLiveEvent
import ru.neotology.nerecipe.adapter.RecipeInteractionListener
import ru.neotology.nerecipe.data.RecipeRepository
import ru.neotology.nerecipe.data.imp.FileRecipeRepository
import ru.neotology.nerecipe.dto.Recipe

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application),
    RecipeInteractionListener {

    private val repository: RecipeRepository =
        FileRecipeRepository(application)

    val data by repository::data

    val navigateToRecipeScreenEvent = SingleLiveEvent<String>()
    val navigateToRecipeSingleScreenEvent = SingleLiveEvent<Recipe>()

    var currentRecipe = MutableLiveData<Recipe?>(null)

    fun onSaveButtonClicked(title: String, category: String, content: String) {
        if (title.isBlank()) return

        val recipe = currentRecipe.value?.copy(
            title = title,
            category = category,
            content = content
        ) ?: Recipe(
            id = RecipeRepository.NEW_RECIPE_ID,
            title = title,
            content = content,
            author = "Администратор",
            category = category
        )
        repository.save(recipe)
        currentRecipe.value = null
    }

    //region RecipeInreractionListener

    fun onAddClicked() {
        navigateToRecipeScreenEvent.call()
    }

    override fun onFavoriteClicked(recipe: Recipe) = repository.favorite(recipe.id)

    override fun onRemoveClicked(recipe: Recipe) = repository.remove(recipe.id)

    override fun onEditClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeScreenEvent.value = recipe.content
    }

    override fun onSingleRecipeShow(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeSingleScreenEvent.value = recipe
    }
    //endregion RecipeInreractionListener
}