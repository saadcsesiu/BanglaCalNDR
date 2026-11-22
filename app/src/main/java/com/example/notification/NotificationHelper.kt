package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.calendar.BanglaCalendarEngine
import com.example.calendar.BanglaDate
import com.example.calendar.toBangla

object NotificationHelper {

    const val CHANNEL_ID = "bangla_calendar_month_end"
    private const val NOTIFICATION_ID = 2901

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_channel_desc)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showMonthEndNotification(
        context: Context,
        banglaDate: BanglaDate = BanglaCalendarEngine.today()
    ) {
        createNotificationChannel(context)

        val nextMonth = banglaDate.month.next()
        val title = "মাস সমাপ্তির বার্তা — ২৯ ${banglaDate.month.banglaName}"
        val content = "আজ ${banglaDate.month.banglaName} মাসের ২৯ তারিখ। আর মাত্র ১-২ দিনের মধ্যেই নতুন মাস ${nextMonth.banglaName} শুরু হতে যাচ্ছে।"

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bangla_calendar_1792002854049)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun showTestNotification(context: Context) {
        val today = BanglaCalendarEngine.today()
        val testBanglaDate = BanglaDate(today.year, today.month, 29)
        showMonthEndNotification(context, testBanglaDate)
    }
}
