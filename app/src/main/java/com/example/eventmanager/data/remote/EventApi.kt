package com.example.eventmanager.data.remote

import com.example.eventmanager.data.model.response.EventDetailDto
import com.example.eventmanager.data.model.response.EventDto
import com.example.eventmanager.data.model.response.EventItem
import com.example.eventmanager.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventApi {

    @GET(Constants.events)
    suspend fun getEvents(@Query("page") page: Int, @Query("search") search: String) : ApiBaseResponse<EventDto>

    @GET(Constants.eventDetails)
    suspend fun getEventDetails(@Path("id") id: String) : ApiBaseResponse<EventDetailDto>
}
