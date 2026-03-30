package com.orioninc.androidapptask.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.orioninc.androidapptask.R

class NotificationHelper(
    private val context: Context,
) {
    companion object {
        const val CHANNEL_ID = "character_refresh_channel"
        const val CHANNEL_NAME = "Character refresh"
        const val CHANNEL_DESCRIPTION = "Notifications for character list refresh"
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT,
                ).apply {
                    description = CHANNEL_DESCRIPTION
                }
            val notificationManager =
                context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun showRefreshSuccess(page: Int) {
        showNotification(
            id = page,
            title = "Character list success",
            message = "Page $page loaded successfully.",
        )
    }

    fun showRefreshError(
        page: Int,
        errorMessage: String,
    ) {
        showNotification(
            id = 1000 + page,
            title = "Character list failed",
            message = "Page $page failed: $errorMessage",
        )
    }

    private fun showNotification(
        id: Int,
        title: String,
        message: String,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED

            if (!granted) return
        }

        val notification =
            NotificationCompat
                .Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

        NotificationManagerCompat.from(context).notify(id, notification)
    }
}
