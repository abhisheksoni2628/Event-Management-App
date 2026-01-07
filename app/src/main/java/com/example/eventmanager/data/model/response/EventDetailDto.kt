package com.example.eventmanager.data.model.response

import com.google.gson.annotations.SerializedName

data class EventDetailDto(
    @SerializedName("event_id") val eventId: String?,
    @SerializedName("event_name") val eventName: String?,
    @SerializedName("event_description") val eventDescription: String?,
    @SerializedName("event_date") val eventDate: String?,
    @SerializedName("event_image_urls") val eventImageUrls: ArrayList<String>?,
    @SerializedName("longitude") val longitude: Double = 0.0,
    @SerializedName("latitude") val latitude: Double = 0.0,
    @SerializedName("event_category_id") val eventCategoryId: String?,
    @SerializedName("event_category") val eventCategory: String?,
    @SerializedName("num_attendees") val numAttendees: Int?,
    @SerializedName("ticket_price") val ticketPrice: Double = 0.0,
    @SerializedName("organizer_contact") val organizerContact: String?,
)
