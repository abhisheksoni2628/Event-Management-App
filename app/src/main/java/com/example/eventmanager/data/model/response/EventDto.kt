package com.example.eventmanager.data.model.response

import com.google.gson.annotations.SerializedName

data class EventDto(
    @SerializedName("num_pages") val numPages: Int?,
    @SerializedName("count") val count: Int?,
    @SerializedName("total_count") val totalCount: Int?,
    @SerializedName("results") val results: List<EventItem>
)
