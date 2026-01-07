package com.example.eventmanager.ui.discover

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventmanager.data.model.response.EventDetailDto
import com.example.eventmanager.data.model.response.EventDto
import com.example.eventmanager.data.model.response.EventItem
import com.example.eventmanager.data.remote.UiState
import com.example.eventmanager.data.repository.EventRepository
import com.example.eventmanager.data.usecase.EventDetailUseCase
import com.example.eventmanager.data.usecase.EventListUseCase
import com.example.eventmanager.utils.Constants.createRandomColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyList

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val eventListUseCase: EventListUseCase, private val eventDetailUseCase: EventDetailUseCase
) : ViewModel() {
    val eventListState = MutableStateFlow<UiState<EventDto>>(UiState.Loading)
    val eventDetailState = MutableStateFlow<UiState<EventDetailDto>>(UiState.Loading)

    var pageCount = 1

    var searchText by mutableStateOf("")
        private set
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedIndex = MutableStateFlow(0)
    val selectedIndex = _selectedIndex.asStateFlow()

    private val _previousIndex = MutableStateFlow(0)
    val previousIndex = _previousIndex.asStateFlow()

    init {
        pageCount = 1
        getEventList(pageCount, "")
        observeSearch()
    }


    var isRefreshing by mutableStateOf(false)
        private set

    fun getEventList(page: Int, search: String) {
        viewModelScope.launch {
            eventListUseCase(page, search).onStart {
                if (page == 1) eventListState.value = UiState.Loading
            }.collect { result ->
                isRefreshing = false
                result.onSuccess { data ->
                    if (data != null) {
                        data.results.forEach { it.tagColor = createRandomColor() }
                        val initialData = when (val state = eventListState.value) {
                            is UiState.Success -> state.data
                            else -> null
                        }

                        val totalResponse = if (initialData == null || page == 1) {
                            data
                        } else {
                            val updatedList = initialData.results + data.results
                            initialData.copy(results = updatedList)
                        }
                        eventListState.value = UiState.Success(totalResponse)
                        pageCount = page
                    }
                }.onFailure {
                    eventListState.value = UiState.Error(it.message.toString())
                }
            }
        }
    }

    private fun observeSearch() {
        viewModelScope.launch {
            searchQuery
                .debounce(600)
                .distinctUntilChanged()
                .collectLatest { query ->
                    pageCount = 1
                    getEventList(pageCount, query)
                }
        }
    }

    fun onSearchTextChange(newText: String) {
        searchText = newText
        _searchQuery.value = newText
    }
    fun refresh() {
        isRefreshing = true
        pageCount = 1
        searchText = ""
        getEventList(pageCount, "")
    }


    fun getEventDetails(id: String) {
        viewModelScope.launch {
            eventDetailUseCase(id).onStart {
                eventDetailState.value = UiState.Loading
            }.collect { result ->
                result.onSuccess { it ->
                    if (it != null) {
                        eventDetailState.value = UiState.Success(it)
                    }
                }.onFailure {
                    eventDetailState.value = UiState.Error(it.message.toString())
                }
            }
        }
    }

    fun selectIndex(index: Int) {
        _previousIndex.value = _selectedIndex.value
        _selectedIndex.value = index
    }

}