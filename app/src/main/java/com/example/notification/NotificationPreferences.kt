package com.example.notification

import android.content.Context
import android.content.SharedPreferences

class NotificationPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("bangla_calendar_prefs", Context.MODE_PRIVATE)

    var isNotificationEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATION_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATION_ENABLED, value).apply()

    var notificationHour: Int
        get() = prefs.getInt(KEY_NOTIFICATION_HOUR, 9)
        set(value) = prefs.edit().putInt(KEY_NOTIFICATION_HOUR, value).apply()

    var notificationMinute: Int
        get() = prefs.getInt(KEY_NOTIFICATION_MINUTE, 0)
        set(value) = prefs.edit().putInt(KEY_NOTIFICATION_MINUTE, value).apply()

    companion object {
        private const val KEY_NOTIFICATION_ENABLED = "key_notification_enabled"
        private const val KEY_NOTIFICATION_HOUR = "key_notification_hour"
        private const val KEY_NOTIFICATION_MINUTE = "key_notification_minute"
    }
}
