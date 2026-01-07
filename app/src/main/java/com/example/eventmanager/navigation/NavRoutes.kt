package com.example.eventmanager.navigation

sealed class NavRoutes(val route: String) {

    data object Discover : NavRoutes("discover")

    data object EventDetails : NavRoutes("event_details/{eventId}/{tagColor}") {
        fun createRoute(eventId: String, tagColor: Int) = "event_details/$eventId/$tagColor"
    }

    data object EventMap : NavRoutes("event_map")
}
