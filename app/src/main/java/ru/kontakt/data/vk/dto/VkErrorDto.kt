package ru.kontakt.data.vk.dto

import com.google.gson.annotations.SerializedName

data class VkErrorDto(
    @SerializedName("error_code")
    val errorCode: Int,

    @SerializedName("error_subcode")
    val errorSubcode: Int,

    @SerializedName("error_msg")
    val errorMsg: String
)