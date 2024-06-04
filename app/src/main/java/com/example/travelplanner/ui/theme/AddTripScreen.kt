package com.example.travelplanner.ui.theme

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.travelplanner.viewmodel.TripViewModel
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.CalendarContract
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.travelplanner.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddTripScreen(navController: NavController, viewModel: TripViewModel) {
    val context = LocalContext.current
    val tripName = remember { mutableStateOf("") }
    val tripDate = remember { mutableStateOf("") }
    val tripCity = remember { mutableStateOf("") }
    val tripCountry = remember { mutableStateOf("") }
    val addToCalendarChecked = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Travel Planner") })
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Dodaj podróż",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = tripName.value,
                    onValueChange = { tripName.value = it },
                    label = { Text("Nazwa podróży") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = tripDate.value,
                    onValueChange = { tripDate.value = it },
                    label = {
                        Column {
                            Text("Data podróży")
                            Text(
                                text = "Format daty: dd.MM.yyyy",
                                style = MaterialTheme.typography.caption,
                                color = Color.Gray
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = tripCity.value,
                    onValueChange = { tripCity.value = it },
                    label = { Text("Miasto") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = tripCountry.value,
                    onValueChange = { tripCountry.value = it },
                    label = { Text("Kraj") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                ) {
                    Checkbox(
                        checked = addToCalendarChecked.value,
                        onCheckedChange = { addToCalendarChecked.value = it }

                    )
                    Text("Dodaj do kalendarza"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    if (tripName.value.isNotBlank() && tripDate.value.isNotBlank() && tripCity.value.isNotBlank() && tripCountry.value.isNotBlank()) {
                        viewModel.addTrip(tripName.value, tripDate.value, tripCity.value, tripCountry.value)
                        if (addToCalendarChecked.value) {
                            addToCalendar(context, tripName.value, tripDate.value)
                        }
                        sendNotification(context, tripName.value, tripDate.value)
                        Toast.makeText(context, "Podróż dodana!", Toast.LENGTH_SHORT).show()
                        tripName.value = ""
                        tripDate.value = ""
                        tripCity.value = ""
                        tripCountry.value = ""
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Wypełnij wszystkie pola!", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Dodaj podróż")
                }
            }
        }
    )
}


fun addToCalendar(context: Context, tripName: String, tripDate: String) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()
    val startDate: Date = dateFormat.parse(tripDate) ?: Date()
    calendar.time = startDate

    val startMillis: Long = calendar.timeInMillis
    val endMillis: Long = startMillis + (24 * 60 * 60 * 1000) // koniec na koniec dnia

    val values = ContentValues().apply {
        put(CalendarContract.Events.DTSTART, startMillis)
        put(CalendarContract.Events.DTEND, endMillis)
        put(CalendarContract.Events.TITLE, tripName)
        put(CalendarContract.Events.DESCRIPTION, "Podróż: $tripName")
        put(CalendarContract.Events.CALENDAR_ID, 1)
        put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
    }

    CoroutineScope(Dispatchers.IO).launch {
        val cr = context.contentResolver
        val uri = cr.insert(CalendarContract.Events.CONTENT_URI, values)
        val eventId = uri?.lastPathSegment?.toLongOrNull()
    }
}

@SuppressLint("MissingPermission")
fun sendNotification(context: Context, tripName: String, tripDate: String) {
    val notificationId = 1
    val channelId = "trip_notifications"
    val channelName = "Trip Notifications"
    val channelDescription = "Notifications for trip additions"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Podróż dodana")
        .setContentText("Podróż '$tripName' została dodana na dzień $tripDate")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}
