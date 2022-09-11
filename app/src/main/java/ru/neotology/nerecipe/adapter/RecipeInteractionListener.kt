package ru.neotology.nerecipe.adapter

import ru.neotology.nerecipe.dto.Recipe

interface RecipeInteractionListener {

    fun onFavoriteClicked (recipe: Recipe)
    fun onRemoveClicked (recipe: Recipe)
    fun onEditClicked (recipe: Recipe)
    fun onSingleRecipeShow(recipe: Recipe)
}