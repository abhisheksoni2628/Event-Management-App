package com.example.eventmanager.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.eventmanager.data.remote.UiState
import com.example.eventmanager.ui.components.GoogleMapView
import com.example.eventmanager.ui.discover.DiscoverViewModel
import com.example.eventmanager.ui.discover.EventItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventMapScreen(
    navController: NavHostController,
    onEventClick: (String, Int) -> Unit,
    viewModel: DiscoverViewModel = hiltViewModel()
) {

    val eventListObserver by viewModel.eventListState.collectAsState()
    val selectedIndex by viewModel.selectedIndex.collectAsState()
    val previousIndex by viewModel.previousIndex.collectAsState()

    LaunchedEffect(Unit) { viewModel.getEventList(viewModel.pageCount, "") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Map View", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (eventListObserver) {
            is UiState.Error -> {}

            UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {

                val eventList = (eventListObserver as UiState.Success).data.results
                val pagerState = rememberPagerState(initialPage = 0, pageCount = { eventList.size })

                LaunchedEffect(pagerState.currentPage) {
                    if (eventList.isNotEmpty() && pagerState.currentPage in eventList.indices) {
                        viewModel.selectIndex(pagerState.currentPage)
                    }
                }

                LaunchedEffect(selectedIndex) {
                    if (eventList.isNotEmpty() && selectedIndex in eventList.indices) {
                        pagerState.animateScrollToPage(selectedIndex)
                    }
                }

                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)) {

                    GoogleMapView(
                        list = eventList,
                        selectedIndex = selectedIndex,
                        previousIndex = previousIndex,
                        onMarkerClick = { index ->
                            viewModel.selectIndex(index)
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 20.dp
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .size(width = 40.dp, height = 4.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color.LightGray)
                                        .align(Alignment.CenterHorizontally)
                                )

                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                ) { page ->
                                    val data = eventList[page]
                                    EventItem(
                                        event = data,
                                        onClick = {
                                            val eventId = data.eventId ?: return@EventItem
                                            onEventClick(eventId, data.tagColor.toArgb())
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}