package com.example.eventmanager.ui.discover

import android.hardware.camera2.params.BlackLevelPattern
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.eventmanager.R
import com.example.eventmanager.data.model.response.EventItem
import com.example.eventmanager.utils.Constants.formatEventDate

@Composable
fun EventItem(
    event: EventItem,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(White), shape = RoundedCornerShape(20.dp), onClick = {
            onClick()
        }) {
        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
        ) {

            SubcomposeAsyncImage(
                modifier = Modifier.width(80.dp),
                model = ImageRequest.Builder(context = LocalContext.current).data(event.eventImageUrl).crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                },
                error = {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                })

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = event.eventName ?: "",
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = event.eventCategory.toString(), fontSize = 12.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .wrapContentWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(event.tagColor)
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                    )
                }

                Row {
                    Text(
                        text = "\u20b9 ${event.ticketPrice}", fontSize = 12.sp, color = Color.Gray
                    )
                    Text(
                        text = formatEventDate(event.eventDate.toString()),
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Box(
                Modifier
                    .height(80.dp)
                    .padding(end = 8.dp), contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    colorFilter = ColorFilter.tint(Color.Gray),
                    contentDescription = null,
                )
            }
        }
    }
}

