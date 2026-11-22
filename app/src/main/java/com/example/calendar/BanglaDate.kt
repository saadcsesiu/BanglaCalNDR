package com.example.calendar

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/**
 * Pure Bangla Date representation.
 */
data class BanglaDate(
    val year: Int,
    val month: BanglaMonth,
    val day: Int
) : Comparable<BanglaDate> {

    val season: BanglaSeason
        get() = BanglaSeason.forMonth(month)

    val banglaDay: String
        get() = day.toBangla()

    val banglaYear: String
        get() = year.toBangla()

    val formattedMonthYear: String
        get() = "${month.banglaName} ${year.toBangla()}"

    val formattedFull: String
        get() = "${day.toBangla()} ${month.banglaName} ${year.toBangla()} বঙ্গাব্দ"

    val isMonthEndAlertDay: Boolean
        get() = day == 29

    /**
     * Corresponding Gregorian date in full English format (e.g. "14 October 2026, Wednesday").
     */
    fun toGregorianFormatted(): String {
        val g = BanglaCalendarEngine.toGregorian(this)
        val monthName = g.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        val dayOfWeek = g.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        return "${g.dayOfMonth} $monthName ${g.year}, $dayOfWeek"
    }

    override fun compareTo(other: BanglaDate): Int {
        if (year != other.year) return year.compareTo(other.year)
        if (month.monthIndex != other.month.monthIndex) return month.monthIndex.compareTo(other.month.monthIndex)
        return day.compareTo(other.day)
    }
}

/**
 * Single cell in the month view grid containing both Bangla and secondary Gregorian dates.
 */
data class CalendarDayCell(
    val banglaDate: BanglaDate,
    val gregorianDate: LocalDate,
    val dayOfWeek: BanglaDayOfWeek,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val isSelected: Boolean,
    val isMonthEndDay: Boolean, // 29th day
    val hasEventOrNote: Boolean = false
) {
    val gregorianDay: Int
        get() = gregorianDate.dayOfMonth

    val gregorianDayText: String
        get() = gregorianDay.toString()
}
