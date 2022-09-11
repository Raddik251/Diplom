package ru.neotology.nerecipe.data

import androidx.lifecycle.LiveData
import ru.neotology.nerecipe.dto.Recipe

interface RecipeRepository {

    val data: LiveData<List<Recipe>>

    fun favorite(recipeId: Int)

    fun remove(recipeId: Int)

    fun save (recipe: Recipe)

    companion object {
        const val NEW_RECIPE_ID = 0
    }
}