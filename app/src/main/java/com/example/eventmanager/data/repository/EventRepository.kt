package com.example.eventmanager.data.repository

import com.example.eventmanager.data.model.response.EventDetailDto
import com.example.eventmanager.data.model.response.EventDto
import com.example.eventmanager.data.model.response.EventItem
import com.example.eventmanager.data.remote.ApiBaseResponse

interface EventRepository {
    suspend fun getEventList(page: Int, search: String): ApiBaseResponse<EventDto>
    suspend fun getEventDetails(id: String): ApiBaseResponse<EventDetailDto>
}