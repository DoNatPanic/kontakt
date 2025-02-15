package ru.kontakt.data.vk.dto

import com.google.gson.annotations.SerializedName

data class NewsFeedDto(

    val items: List<NewsFeedItemDto>,

    @SerializedName("next_from")
    val nextFrom: String,

    val error: VkErrorDto?,
)