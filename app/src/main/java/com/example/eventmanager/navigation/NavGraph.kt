package com.example.eventmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.eventmanager.ui.details.EventDetailScreen
import com.example.eventmanager.ui.discover.DiscoverScreen
import com.example.eventmanager.ui.map.EventMapScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Discover.route
    ) {

        composable(NavRoutes.Discover.route) {
            DiscoverScreen(
                onEventClick = { eventId, tagColor ->
                    navController.navigate(
                        NavRoutes.EventDetails.createRoute(eventId, tagColor)
                    )
                },
                onMapClick = {
                    navController.navigate(NavRoutes.EventMap.route)
                }
            )
        }

        composable(
            route = NavRoutes.EventDetails.route,
            arguments = listOf(
                navArgument("eventId") {
                    type = NavType.StringType
                },
                navArgument("tagColor") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val eventId = backStackEntry.arguments
                ?.getString("eventId")
                ?: return@composable

            val tagColor = backStackEntry.arguments
                ?.getInt("tagColor")
                ?: return@composable

            EventDetailScreen(
                navController = navController,
                data = eventId,
                tagColor = tagColor
            )
        }

        composable(NavRoutes.EventMap.route) {
            EventMapScreen(
                navController = navController,
                onEventClick = { eventId, tagColor ->
                    navController.navigate(
                        NavRoutes.EventDetails.createRoute(eventId, tagColor)
                    )
                }
            )
        }
    }
}
