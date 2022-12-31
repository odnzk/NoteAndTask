package com.example.noteapp.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.noteapp.R
import javax.inject.Inject

class NotificationProvider @Inject constructor(
    private val context: Context
) {

    init {
        createNotificationChannel()
    }
    // todoItem notification:
    // 1) title: totoItem title
    // 2) icon: app icon?
    // 3) contentText: notification??
    // 4) action: navigate to app and open that todoItem

    // todo maybe make all more abstract
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_reminders_name)
            val descriptionText =
                context.getString(R.string.notification_channel_reminders_description)
            val mChannel = NotificationChannel(
                context.getString(R.string.notification_channel_reminders_id),
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    fun createNotification(title: String, contentText: String, intent: Intent): Notification {
//        val intent = Intent(this, AlertDetails::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(
            context,
            context.getString(R.string.notification_channel_reminders_id)
        )
            .setSmallIcon(R.drawable.ic_outline_task)
            .setContentTitle(title)
            .setContentText(contentText)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).build()
    }


}
