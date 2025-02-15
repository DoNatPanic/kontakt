package ru.kontakt.data.vk.dto

import com.google.gson.annotations.SerializedName

enum class NewsFeedFilterDto(val value: String) {
    @SerializedName("post")
    POST("post")
}