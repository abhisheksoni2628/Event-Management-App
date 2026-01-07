package com.example.eventmanager.data.usecase

import com.example.eventmanager.data.repository.EventRepository
import com.example.eventmanager.utils.Constants.SUCCESS
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class EventListUseCase @Inject constructor(private val repository: EventRepository) {

    operator fun invoke(page: Int, search: String) = flow {
        val response = repository.getEventList(page, search)
        if (response.status == SUCCESS) {
            emit(Result.success(response.response))
        } else {
            emit(Result.failure(kotlin.Exception(response.message)))
        }
    }
}