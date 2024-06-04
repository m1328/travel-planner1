package com.example.travelplanner

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun checkAndRequestPermissions(activity: Activity) {
    val permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR)

    if (permission != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CALENDAR), 1)
    }
}
