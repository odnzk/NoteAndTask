package com.example.noteapp.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.noteapp.R
import javax.inject.Inject

class NotificationProvider @Inject constructor(
    private val context: Context
) {
    // todoItem notification:
    // 1) title: totoItem title
    // 2) icon: app icon?
    // 3) contentText: notification??
    // 4) action: navigate to app and open that todoItem

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

    // todo set notification id
    fun showNotification(title: String, contentText: String, intent: Intent) {
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, createNotification(title, contentText, intent))
        }
    }


}
