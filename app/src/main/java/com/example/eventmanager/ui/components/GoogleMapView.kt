package com.example.eventmanager.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.eventmanager.data.model.response.EventItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlin.collections.getOrNull
import kotlin.collections.indices
import kotlin.ranges.coerceIn

@Composable
fun GoogleMapView(
    list: List<EventItem>,
    selectedIndex: Int,
    previousIndex: Int,
    onMarkerClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState()
    val markerState = rememberMarkerState()

    LaunchedEffect(selectedIndex) {
        if (selectedIndex !in list.indices) return@LaunchedEffect

        val previousLatLng = LatLng(
            list[previousIndex.coerceIn(list.indices)].latitude, list[previousIndex.coerceIn(list.indices)].longitude
        )
        cameraPositionState.animate(update = CameraUpdateFactory.newLatLngZoom(previousLatLng, 4f), durationMs = 800)

        val selectedLatLng = LatLng(list[selectedIndex].latitude, list[selectedIndex].longitude)
        cameraPositionState.animate(update = CameraUpdateFactory.newLatLngZoom(selectedLatLng, 8f), durationMs = 1000)
        markerState.position = selectedLatLng
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(), cameraPositionState = cameraPositionState, uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        )
    ) {
        Marker(
            state = markerState,
            title = list.getOrNull(selectedIndex)?.eventName,
            snippet = list.getOrNull(selectedIndex)?.eventCategory,
            onClick = {
                onMarkerClick(selectedIndex)
                true
            })
    }
}