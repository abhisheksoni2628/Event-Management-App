package com.example.eventmanager.data.model.response

import androidx.compose.ui.graphics.Color
import com.example.eventmanager.utils.Constants.createRandomColor
import com.google.gson.annotations.SerializedName

data class EventItem(
    @SerializedName("event_id")
    val eventId: String?,
    @SerializedName("event_name")
    val eventName: String?,
    @SerializedName("event_date")
    val eventDate: String?,
    @SerializedName("event_image_url")
    val eventImageUrl: String?,
    @SerializedName("longitude")
    val longitude: Double = 0.0,
    @SerializedName("latitude")
    val latitude: Double = 0.0,
    @SerializedName("event_category_id")
    val eventCategoryId: String?,
    @SerializedName("event_category")
    val eventCategory: String?,
    @SerializedName("ticket_price")
    val ticketPrice: Double?,
    var tagColor: Color = createRandomColor()
)