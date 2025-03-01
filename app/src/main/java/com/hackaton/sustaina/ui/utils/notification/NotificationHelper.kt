package com.hackaton.sustaina.ui.utils.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hackaton.sustaina.R

class NotificationHelper(private val context: Context) {
    private val channelId = "hotspot_alerts"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Hotspot Alerts"
            val descriptionText = "Notifications for garbage hotspot zones"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Do not delete! This is used to send the notification of hotspot
    @SuppressLint("MissingPermission")
    fun sendHotspotNotification(hotspotName: String) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.icon_landing)
            .setContentTitle("Garbage Hotspot Alert!")
            .setContentText("You're in a hotspot zone: $hotspotName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(1234, notification) // ID must be unique per notification
        }
    }
}
