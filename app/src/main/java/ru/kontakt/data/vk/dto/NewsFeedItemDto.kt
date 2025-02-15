package ru.kontakt.data.vk.dto

import com.google.gson.annotations.SerializedName

data class NewsFeedItemDto(
    val type: String,

    @SerializedName("source_id")
    val sourceId: Int,

    @SerializedName("post_id")
    val postId: Int,

    val text: String
)