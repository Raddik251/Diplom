package ru.neotology.nerecipe.dto

import kotlinx.serialization.Serializable

@Serializable
data class Recipe (
    var id: Long,
    var title: String,
    var content: String,
    var author: String,
    var category: String,
    var favoriteToMe: Boolean = false
)