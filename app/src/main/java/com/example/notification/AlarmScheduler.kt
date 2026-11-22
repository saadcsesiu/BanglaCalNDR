package com.example.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.calendar.BanglaCalendarEngine
import com.example.calendar.BanglaDate
import com.example.calendar.BanglaMonth
import com.example.calendar.toBangla
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

object AlarmScheduler {

    private const val REQUEST_CODE_MONTH_END = 29001

    fun scheduleNextMonthEndAlarm(context: Context): BanglaDate? {
        val prefs = NotificationPreferences(context)
        if (!prefs.isNotificationEnabled) {
            cancelAlarm(context)
            return null
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val now = LocalDateTime.now()
        val todayGregorian = now.toLocalDate()
        val currentBangla = BanglaCalendarEngine.fromGregorian(todayGregorian)

        val targetHour = prefs.notificationHour
        val targetMinute = prefs.notificationMinute

        // Determine if current month 29th is still in the future
        var candidateBangla = BanglaDate(currentBangla.year, currentBangla.month, 29)
        var candidateGregorian = BanglaCalendarEngine.toGregorian(candidateBangla)
        var candidateDateTime = LocalDateTime.of(candidateGregorian, LocalTime.of(targetHour, targetMinute))

        if (candidateDateTime.isBefore(now)) {
            // Need next month's 29th
            val nextMonth = currentBangla.month.next()
            val nextYear = if (nextMonth == BanglaMonth.BOISHAKH) currentBangla.year + 1 else currentBangla.year
            candidateBangla = BanglaDate(nextYear, nextMonth, 29)
            candidateGregorian = BanglaCalendarEngine.toGregorian(candidateBangla)
            candidateDateTime = LocalDateTime.of(candidateGregorian, LocalTime.of(targetHour, targetMinute))
        }

        val triggerEpochMillis = candidateDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val intent = Intent(context, BanglaMonthEndReceiver::class.java).apply {
            putExtra("year", candidateBangla.year)
            putExtra("monthIndex", candidateBangla.month.monthIndex)
            putExtra("day", candidateBangla.day)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_MONTH_END,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerEpochMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerEpochMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerEpochMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            // Fallback for strict exact alarm restrictions
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerEpochMillis,
                pendingIntent
            )
        }

        return candidateBangla
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BanglaMonthEndReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_MONTH_END,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    fun getNextScheduledBanglaDate(context: Context): BanglaDate {
        val prefs = NotificationPreferences(context)
        val now = LocalDateTime.now()
        val todayGregorian = now.toLocalDate()
        val currentBangla = BanglaCalendarEngine.fromGregorian(todayGregorian)

        val targetHour = prefs.notificationHour
        val targetMinute = prefs.notificationMinute

        var candidateBangla = BanglaDate(currentBangla.year, currentBangla.month, 29)
        var candidateGregorian = BanglaCalendarEngine.toGregorian(candidateBangla)
        var candidateDateTime = LocalDateTime.of(candidateGregorian, LocalTime.of(targetHour, targetMinute))

        if (candidateDateTime.isBefore(now)) {
            val nextMonth = currentBangla.month.next()
            val nextYear = if (nextMonth == BanglaMonth.BOISHAKH) currentBangla.year + 1 else currentBangla.year
            candidateBangla = BanglaDate(nextYear, nextMonth, 29)
        }

        return candidateBangla
    }
}
