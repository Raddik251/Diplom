package ru.neotology.nerecipe.data.imp

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.neotology.nerecipe.data.RecipeRepository
import ru.neotology.nerecipe.dto.Recipe
import kotlin.properties.Delegates

class FileRecipeRepository (
    private val application: Application
        ) : RecipeRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Recipe::class.java).type

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )

    private var nextId: Int by Delegates.observable(
        prefs.getInt(NEXT_ID_PREFS_KEY, 0)
    ) { _, _, newValue ->
        prefs.edit { putInt(NEXT_ID_PREFS_KEY, newValue) }
    }

    private var recipes
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    set(value) {
        application.openFileOutput(
            FILE_NAME, Context.MODE_PRIVATE
        ).bufferedWriter().use {
            it.write(gson.toJson(value))
        }
        data.value = value
    }

    override val data: MutableLiveData<List<Recipe>>

    init {
        val recipesFile = application.filesDir.resolve(FILE_NAME)
        val recipes: List<Recipe> = if (recipesFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use {gson.fromJson(it, type)}
        } else emptyList()

        data = MutableLiveData(recipes)
    }

    override fun favorite(recipeId: Int) {
        recipes = recipes.map {
            if (it.id != recipeId) it
            else it.copy(
                favoriteToMe = !it.favoriteToMe
            )
        }
    }

    override fun remove(recipeId: Int) {
        recipes = recipes.filter { it.id != recipeId }
    }

    override fun save(recipe: Recipe) {

        if (recipe.id == RecipeRepository.NEW_RECIPE_ID) insert(recipe) else update(recipe)
    }

    private fun insert(recipe: Recipe) {

        recipes = listOf(
            recipe.copy(
                id = ++nextId
            )
        ) + recipes
    }

    private fun update(recipe: Recipe) {

        recipes = recipes.map {
            if (it.id == recipe.id) recipe else it
        }
    }

    companion object {
        private const val NEXT_ID_PREFS_KEY = "nextId"
        private const val FILE_NAME = "recipes.json"
    }

}