package com.example.travelplanner

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.travelplanner.data.Trip
import com.example.travelplanner.ui.theme.AddTripScreen
import com.example.travelplanner.ui.theme.MainScreen
import com.example.travelplanner.ui.theme.PackingListScreen
import com.example.travelplanner.ui.theme.TravelPlannerTheme
import com.example.travelplanner.ui.theme.ViewTripsScreen
import com.example.travelplanner.viewmodel.PackingListViewModel
import com.example.travelplanner.viewmodel.TripViewModel


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelPlannerTheme {
                val navController = rememberNavController()
                val tripViewModel: TripViewModel = viewModel()
                val packingListViewModel: PackingListViewModel = viewModel()

                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) {
                    NavigationHost(
                        navController = navController,
                        tripViewModel = tripViewModel,
                        trips = tripViewModel.trips,
                        packingListViewModel = packingListViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Strona Główna", Icons.Default.Home, "main"),
        BottomNavItem("Dodaj Podróż", Icons.Default.Add, "addTrip"),
        BottomNavItem("Lista Pakowania", Icons.Default.Backpack, "packingList")
    )

    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun NavigationHost(navController: NavHostController, tripViewModel: TripViewModel, trips: LiveData<List<Trip>>,packingListViewModel: PackingListViewModel) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController, trips, tripViewModel) }
        composable("addTrip") { AddTripScreen(navController, tripViewModel) }
        composable("viewTrips") {
            ViewTripsScreen(tripViewModel.trips, tripViewModel)
        }
        composable("packingList") { PackingListScreen(viewModel = packingListViewModel) }
    }
}

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)
