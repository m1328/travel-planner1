package com.example.travelplanner.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelplanner.data.Trip
import com.example.travelplanner.viewmodel.TripViewModel
//import androidx.compose.runtime.observeAsState
import androidx.lifecycle.LiveData
@Composable
fun ViewTripsScreen(trips: LiveData<List<Trip>>, viewModel: TripViewModel) {
    val tripList by trips.observeAsState(emptyList())

    LazyColumn {
        items(tripList) { trip ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = 4.dp,
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface
            ) {
                TripItem(trip, viewModel)
            }
        }
    }
}

@Composable
fun TripItem(trip: Trip, viewModel: TripViewModel) {
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        EditTripDialog(
            trip = trip,
            onDismiss = { showDialog.value = false },
            onSave = { editedTrip ->
                viewModel.updateTrip(editedTrip)
                showDialog.value = false
            }
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(trip.name, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Data: ${trip.date}", style = MaterialTheme.typography.body2)
            Text("Miasto: ${trip.city}", style = MaterialTheme.typography.body2)
            Text("Kraj: ${trip.country}", style = MaterialTheme.typography.body2)
        }
        Row {
            IconButton(onClick = { showDialog.value = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = { viewModel.deleteTrip(trip) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun EditTripDialog(
    trip: Trip,
    onDismiss: () -> Unit,
    onSave: (Trip) -> Unit
) {
    var editedTrip by remember { mutableStateOf(trip) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edycja podróży") },
        confirmButton = {
            Button(
                onClick = {
                    onSave(editedTrip)
                    onDismiss()
                }
            ) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Anuluj")
            }
        },
        text = {
            Column {
                TextField(
                    value = editedTrip.name,
                    onValueChange = { editedTrip = editedTrip.copy(name = it) },
                    label = { Text("Nazwa podróży") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = editedTrip.date,
                    onValueChange = { editedTrip = editedTrip.copy(date = it) },
                    label = { Text("Data podróży") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = editedTrip.city,
                    onValueChange = { editedTrip = editedTrip.copy(city = it) },
                    label = { Text("Miasto") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = editedTrip.country,
                    onValueChange = { editedTrip = editedTrip.copy(country = it) },
                    label = { Text("Kraj") }
                )
            }
        }
    )
}
