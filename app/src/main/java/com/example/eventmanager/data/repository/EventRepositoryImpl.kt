package com.example.eventmanager.data.repository

import com.example.eventmanager.data.model.response.EventDetailDto
import com.example.eventmanager.data.model.response.EventDto
import com.example.eventmanager.data.model.response.EventItem
import com.example.eventmanager.data.remote.ApiBaseResponse
import com.example.eventmanager.data.remote.EventApi

class EventRepositoryImpl(
    private val apiService: EventApi
) : EventRepository {

    override suspend fun getEventList(page: Int, search: String): ApiBaseResponse<EventDto> {
        return apiService.getEvents(page, search)
    }

    override suspend fun getEventDetails(id: String): ApiBaseResponse<EventDetailDto> {
        return apiService.getEventDetails(id)
    }
}