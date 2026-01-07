package com.example.eventmanager.utils

import androidx.compose.ui.graphics.Color
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

object Constants {
    const val BASE_URL = "http://13.220.198.252"

    const val events = "/events"
    const val eventDetails = "/events/{id}"
    const val SUCCESS = "success"

    fun formatEventDate(dateString: String): String {
        return try {
            val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            val outputFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")

            val date = OffsetDateTime.parse(dateString, inputFormatter)
            date.format(outputFormatter)
        } catch (e: Exception) {
            ""
        }
    }

    fun createRandomColor(): Color {
        val redChannel = Random.nextInt(from = 200, until = 256)
        val greenChannel = Random.nextInt(from = 200, until = 256)
        val blueChannel = Random.nextInt(from = 200, until = 256)

        return Color(
            red = redChannel,
            green = greenChannel,
            blue = blueChannel
        )
    }

}