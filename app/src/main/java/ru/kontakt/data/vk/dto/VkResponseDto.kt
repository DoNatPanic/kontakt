package ru.kontakt.data.vk.dto

data class VkResponseDto<T>(
    val response: T?,
    val error: VkErrorDto?
)