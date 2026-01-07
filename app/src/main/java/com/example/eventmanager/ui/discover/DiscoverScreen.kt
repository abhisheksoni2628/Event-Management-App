package com.example.eventmanager.ui.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eventmanager.data.remote.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    onEventClick: (String, Int) -> Unit,
    onMapClick: () -> Unit,
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    val scrollState = rememberLazyListState()
    val eventListObserver by viewModel.eventListState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Discover", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = onMapClick) {
                        Icon(
                            Icons.Filled.Map,
                            contentDescription = "Map"
                        )
                    }
                }
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            DiscoverSearchBar(
                value = viewModel.searchText,
                onValueChange = {
                    viewModel.onSearchTextChange(it)
                }
            )

            when (eventListObserver) {

                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Success -> {
                    val eventList = (eventListObserver as UiState.Success).data
                    PullToRefreshBox(
                        isRefreshing = viewModel.isRefreshing,
                        onRefresh = {
                            viewModel.refresh()
                        }
                    ) {
                            LazyColumn(
                                state = scrollState,
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                itemsIndexed(eventList.results) { index, item ->
                                    EventItem(
                                        event = item,
                                        onClick = { onEventClick(item.eventId ?: "", item.tagColor.toArgb()) },
                                        modifier = Modifier
                                    )

                                    if (index == eventList.results.lastIndex) {
                                        viewModel.getEventList(viewModel.pageCount + 1, viewModel.searchText)
                                        return@itemsIndexed
                                    }
                                }

                                if(viewModel.pageCount > 1) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }
                            }
                    }
                }

                is UiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Text(text = (eventListObserver as UiState.Error).message)
                    }
                }
            }
        }
    }
}
