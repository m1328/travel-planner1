package com.example.travelplanner.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.travelplanner.R
import com.example.travelplanner.data.Trip
import com.example.travelplanner.viewmodel.TripViewModel
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController, trips: LiveData<List<Trip>>, viewModel: TripViewModel) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Travel Planner") }) },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image1),
                    contentDescription = "Image 1",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Twoje zaplanowane podróże",
                    style = MaterialTheme.typography.h5.copy(
                        color = Color.Black,
                        //fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                ViewTripsScreen(trips, viewModel)
            }
        }
    )
}

