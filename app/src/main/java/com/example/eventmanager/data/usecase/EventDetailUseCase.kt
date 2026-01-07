package com.example.eventmanager.data.usecase

import com.example.eventmanager.data.repository.EventRepository
import com.example.eventmanager.utils.Constants.SUCCESS
import jakarta.inject.Inject
import kotlinx.coroutines.flow.flow

class EventDetailUseCase @Inject constructor(private val repository: EventRepository) {
    operator fun invoke(id: String) = flow {
        val response = repository.getEventDetails(id)
        if (response.status == SUCCESS) {
            emit(Result.success(response.response))
        } else {
            emit(Result.failure(kotlin.Exception(response.message)))
        }
    }
}