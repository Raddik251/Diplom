package ru.neotology.nerecipe.dto

import kotlinx.serialization.Serializable

@Serializable
data class Recipe (
    val id: Int,
    val title: String = "Название рецепта",
    val content: String = "Текст рецепта",
    val author: String = "Администратор",
    val category: String = "Общая кухня",
    var favoriteToMe: Boolean = false
)