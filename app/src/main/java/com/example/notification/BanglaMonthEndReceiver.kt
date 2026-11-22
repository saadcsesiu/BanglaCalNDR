package com.example.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.calendar.BanglaCalendarEngine
import com.example.calendar.BanglaDate
import com.example.calendar.BanglaMonth

class BanglaMonthEndReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val prefs = NotificationPreferences(context)
        if (!prefs.isNotificationEnabled) return

        val year = intent?.getIntExtra("year", -1) ?: -1
        val monthIndex = intent?.getIntExtra("monthIndex", -1) ?: -1
        val day = intent?.getIntExtra("day", 29) ?: 29

        val triggerBanglaDate = if (year != -1 && monthIndex != -1) {
            BanglaDate(year, BanglaMonth.fromIndex(monthIndex), day)
        } else {
            val today = BanglaCalendarEngine.today()
            BanglaDate(today.year, today.month, 29)
        }

        // Show Bangla month end notification
        NotificationHelper.showMonthEndNotification(context, triggerBanglaDate)

        // Automatically reschedule for next month's 29th day
        AlarmScheduler.scheduleNextMonthEndAlarm(context)
    }
}
