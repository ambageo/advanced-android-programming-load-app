package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(message:String, context: Context) {

    val intent = Intent(context, DetailActivity::class.java)

    val pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    // Build the notification
    val builder = NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
    builder.setSmallIcon(R.drawable.cloud_download)
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText(message)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .addAction(0, "Open to see details", pendingIntent)
        .priority = NotificationCompat.PRIORITY_HIGH

    notify(NOTIFICATION_ID, builder.build())
}