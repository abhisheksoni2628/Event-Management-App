package com.example.eventmanager.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.eventmanager.R
import com.example.eventmanager.data.remote.UiState
import com.example.eventmanager.ui.components.HorizontalPagerIndicator
import com.example.eventmanager.ui.discover.DiscoverViewModel
import com.example.eventmanager.ui.theme.Primary
import com.example.eventmanager.ui.theme.PrimaryDark
import com.example.eventmanager.utils.Constants.formatEventDate
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    navController: NavHostController,
    viewModel: DiscoverViewModel = hiltViewModel(),
    data: String?,
    tagColor: Int
) {

    val eventDetailObserver by viewModel.eventDetailState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Event Details", fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
        val innerPadding = innerPadding

        LaunchedEffect(data) {
            data?.let { viewModel.getEventDetails(it) }
        }

        when (eventDetailObserver) {
            is UiState.Error -> {}

            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                val data = (eventDetailObserver as UiState.Success).data
                val imageList = data.eventImageUrls ?: emptyList()
                val pagerState = rememberPagerState(pageCount = { imageList.size })

                LaunchedEffect(imageList.size) {
                    if (imageList.isEmpty()) return@LaunchedEffect
                    while (true) {
                        delay(5000L)
                        val next = (pagerState.currentPage + 1) % imageList.size
                        pagerState.animateScrollToPage(next)
                    }
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier.padding(innerPadding)
                ) {
                    Column(
                        modifier = Modifier.background(White)
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) { page ->
                            SubcomposeAsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(data.eventImageUrls?.get(page)).crossfade(true).build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                loading = {
                                    Box(modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator()
                                    }
                                },
                                error = {
                                    Image(
                                        painter = painterResource(R.drawable.ic_launcher_background),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = null
                                    )
                                })
                        }

                        if (imageList.size > 1) {
                            HorizontalPagerIndicator(
                                pagerState = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }

                        Column(
                            modifier = Modifier.padding(12.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = data.eventName.toString(),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = data.eventCategory.toString(), textAlign = TextAlign.End,
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(tagColor))
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 16.sp,
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "\u20b9 ${data.ticketPrice}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray
                                )
                                Text(
                                    text = formatEventDate(data.eventDate.toString()),
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth(),
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = data.eventDescription.toString(),
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Reach out to learn more",
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = data.organizerContact.toString(),
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${data.numAttendees} others are attending!",
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                color = PrimaryDark,
                                textAlign = TextAlign.Center
                            )

                            Button(
                                onClick = {
                                    navController.popBackStack()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Primary
                                )
                            ) {
                                Text("Buy Your Ticket", color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}