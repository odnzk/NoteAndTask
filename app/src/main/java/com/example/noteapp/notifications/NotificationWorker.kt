package com.example.noteapp.notifications

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.noteapp.MainActivity

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val title = inputData.getString(NOTIFICATION_TITLE_KEY) ?: return Result.failure()
        val content = inputData.getString(NOTIFICATION_CONTENT_KEY) ?: return Result.failure()
        // todo intent to selected todoItem
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        NotificationProvider(applicationContext).showNotification(title, content, intent)
        return Result.success()
    }

    companion object {
        const val NOTIFICATION_TITLE_KEY = "notification title"
        const val NOTIFICATION_CONTENT_KEY = "notification title"
    }
}
