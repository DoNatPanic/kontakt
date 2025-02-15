package ru.kontakt.data.vk

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.kontakt.data.vk.dto.NewsFeedDto
import ru.kontakt.data.vk.dto.NewsFeedFilterDto
import ru.kontakt.data.vk.dto.VkResponseDto

interface VkAPI {
    @GET("newsfeed.get")
    suspend fun getNewsFeed(
        @Query("filters") filters: NewsFeedFilterDto?,  // FIXME add list support
        @Header("Authorization") auth: String,
        @Query("count") count: Int? = null,
        @Query("start_from") startFrom: String? = null,
        @Query("v") version: String = "5.199",
    ): VkResponseDto<NewsFeedDto>
}