package com.example.calendar

import java.time.LocalDate
import java.time.Year
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

/**
 * Pure Bangla Calendar computation engine based on the Bangladesh Bangla Academy revised standard.
 * - Year starts on 1st Boishakh (April 14).
 * - Months 1 to 6 (Boishakh to Ashwin) have 31 days each.
 * - Months 7 to 10 (Kartik to Magh) have 30 days each.
 * - Month 11 (Falgun) has 29 days (30 days in leap year).
 * - Month 12 (Choitra) has 30 days.
 * - 100% offline, lightweight, deterministic, O(1) performance with zero battery impact.
 */
object BanglaCalendarEngine {

    const val BASE_PAGE_YEAR = 1400
    const val TOTAL_PAGES = 3600

    fun pageToYearMonth(page: Int): Pair<Int, BanglaMonth> {
        val year = BASE_PAGE_YEAR + (page / 12)
        val monthIndex = (page % 12) + 1
        return Pair(year, BanglaMonth.fromIndex(monthIndex))
    }

    fun yearMonthToPage(year: Int, month: BanglaMonth): Int {
        val clampedYear = year.coerceIn(BASE_PAGE_YEAR, BASE_PAGE_YEAR + (TOTAL_PAGES / 12) - 1)
        return (clampedYear - BASE_PAGE_YEAR) * 12 + (month.monthIndex - 1)
    }

    /**
     * Determines whether the given Bangla year has a leap year (extra day in Falgun).
     * Falgun of Bangla year Y occurs in February of Gregorian year (Y + 594).
     */
    fun isLeapYear(banglaYear: Int): Boolean {
        val gregorianYear = banglaYear + 594
        return Year.of(gregorianYear).isLeap
    }

    /**
     * Returns total days in a given Bangla month and year.
     */
    fun daysInMonth(year: Int, month: BanglaMonth): Int {
        return when (month) {
            BanglaMonth.BOISHAKH,
            BanglaMonth.JAISHTHA,
            BanglaMonth.ASHARH,
            BanglaMonth.SHRABON,
            BanglaMonth.BHADRO,
            BanglaMonth.ASHWIN -> 31

            BanglaMonth.KARTIK,
            BanglaMonth.AGRAHAYAN,
            BanglaMonth.POUSH,
            BanglaMonth.MAGH -> 30

            BanglaMonth.FALGUN -> if (isLeapYear(year)) 30 else 29
            BanglaMonth.CHOITRA -> 30
        }
    }

    /**
     * Converts a Gregorian LocalDate to pure BanglaDate.
     */
    fun fromGregorian(gregorianDate: LocalDate): BanglaDate {
        val gYear = gregorianDate.year
        val april14CurrentYear = LocalDate.of(gYear, 4, 14)

        val (banglaYear, baseDate) = if (gregorianDate.isBefore(april14CurrentYear)) {
            Pair(gYear - 594, LocalDate.of(gYear - 1, 4, 14))
        } else {
            Pair(gYear - 593, april14CurrentYear)
        }

        var remainingDays = ChronoUnit.DAYS.between(baseDate, gregorianDate).toInt()

        for (m in BanglaMonth.values()) {
            val count = daysInMonth(banglaYear, m)
            if (remainingDays < count) {
                return BanglaDate(
                    year = banglaYear,
                    month = m,
                    day = remainingDays + 1
                )
            }
            remainingDays -= count
        }

        // Fallback safety (should not normally be reached)
        return BanglaDate(banglaYear, BanglaMonth.CHOITRA, 30)
    }

    /**
     * Converts a BanglaDate back to Gregorian LocalDate.
     */
    fun toGregorian(banglaDate: BanglaDate): LocalDate {
        val baseYear = banglaDate.year + 593
        val baseDate = LocalDate.of(baseYear, 4, 14)

        var dayOffset = 0L
        for (m in BanglaMonth.values()) {
            if (m == banglaDate.month) break
            dayOffset += daysInMonth(banglaDate.year, m)
        }
        dayOffset += (banglaDate.day - 1)

        return baseDate.plusDays(dayOffset)
    }

    /**
     * Returns the BanglaDayOfWeek for a given BanglaDate.
     */
    fun getDayOfWeek(banglaDate: BanglaDate): BanglaDayOfWeek {
        val localDate = toGregorian(banglaDate)
        return BanglaDayOfWeek.fromJavaDayOfWeek(localDate.dayOfWeek)
    }

    /**
     * Returns the days remaining until the end of this Bangla month.
     */
    fun getDaysRemainingInMonth(banglaDate: BanglaDate): Int {
        val totalDays = daysInMonth(banglaDate.year, banglaDate.month)
        return totalDays - banglaDate.day
    }

    /**
     * Today in Bangla Date.
     */
    fun today(): BanglaDate {
        return fromGregorian(LocalDate.now())
    }

    /**
     * Finds the next upcoming 29th day of a Bangla month starting from [fromDate].
     */
    fun getNext29thDay(fromDate: LocalDate = LocalDate.now()): Pair<BanglaDate, LocalDate> {
        val currentBangla = fromGregorian(fromDate)
        val todayGregorian = fromDate

        // Check if current month's 29th is still in the future or today
        val candidateThisMonth = BanglaDate(currentBangla.year, currentBangla.month, 29)
        val candidateGregorian = toGregorian(candidateThisMonth)

        return if (!candidateGregorian.isBefore(todayGregorian)) {
            Pair(candidateThisMonth, candidateGregorian)
        } else {
            // Next month's 29th
            val nextMonth = currentBangla.month.next()
            val nextYear = if (nextMonth == BanglaMonth.BOISHAKH) currentBangla.year + 1 else currentBangla.year
            val candidateNext = BanglaDate(nextYear, nextMonth, 29)
            Pair(candidateNext, toGregorian(candidateNext))
        }
    }

    /**
     * Returns the Gregorian month and year span covering a specific Bangla month
     * e.g. "September – October 2026" or "December 2026 – January 2027"
     */
    fun getGregorianMonthSpanText(year: Int, month: BanglaMonth): String {
        val startDate = toGregorian(BanglaDate(year, month, 1))
        val endDate = toGregorian(BanglaDate(year, month, daysInMonth(year, month)))

        val startMonthName = startDate.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        val endMonthName = endDate.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)

        return if (startDate.year == endDate.year) {
            if (startDate.month == endDate.month) {
                "$startMonthName ${startDate.year}"
            } else {
                "$startMonthName – $endMonthName ${startDate.year}"
            }
        } else {
            "$startMonthName ${startDate.year} – $endMonthName ${endDate.year}"
        }
    }

    /**
     * Generates a complete grid of calendar cells (7 columns x rows) for a given Bangla month and year.
     * Weeks start on Sunday (রবিবার).
     */
    fun getMonthGrid(
        year: Int,
        month: BanglaMonth,
        selectedDate: BanglaDate,
        today: BanglaDate,
        notedDays: Set<Int> = emptySet()
    ): List<CalendarDayCell> {
        val daysInThisMonth = daysInMonth(year, month)
        val firstDayBangla = BanglaDate(year, month, 1)
        val firstDayOfWeek = getDayOfWeek(firstDayBangla) // 1=Sunday, 2=Monday, ... 7=Saturday

        // Sunday is index 1, so offset = (firstDayOfWeek.dayIndex - 1)
        val leadingDaysCount = firstDayOfWeek.dayIndex - 1

        val cells = mutableListOf<CalendarDayCell>()

        // 1. Trailing days from previous month
        if (leadingDaysCount > 0) {
            val prevMonth = month.previous()
            val prevYear = if (prevMonth == BanglaMonth.CHOITRA) year - 1 else year
            val prevMonthDaysCount = daysInMonth(prevYear, prevMonth)
            val startDay = prevMonthDaysCount - leadingDaysCount + 1

            for (d in startDay..prevMonthDaysCount) {
                val bDate = BanglaDate(prevYear, prevMonth, d)
                val gDate = toGregorian(bDate)
                val dow = BanglaDayOfWeek.fromJavaDayOfWeek(gDate.dayOfWeek)
                cells.add(
                    CalendarDayCell(
                        banglaDate = bDate,
                        gregorianDate = gDate,
                        dayOfWeek = dow,
                        isCurrentMonth = false,
                        isToday = bDate == today,
                        isSelected = bDate == selectedDate,
                        isMonthEndDay = bDate.day == 29,
                        hasEventOrNote = false
                    )
                )
            }
        }

        // 2. Days of the current month
        val firstGregorian = toGregorian(firstDayBangla)
        for (d in 1..daysInThisMonth) {
            val bDate = BanglaDate(year, month, d)
            val gDate = firstGregorian.plusDays((d - 1).toLong())
            val dow = BanglaDayOfWeek.fromJavaDayOfWeek(gDate.dayOfWeek)
            cells.add(
                CalendarDayCell(
                    banglaDate = bDate,
                    gregorianDate = gDate,
                    dayOfWeek = dow,
                    isCurrentMonth = true,
                    isToday = bDate == today,
                    isSelected = bDate == selectedDate,
                    isMonthEndDay = bDate.day == 29,
                    hasEventOrNote = notedDays.contains(d)
                )
            )
        }

        // 3. Leading days of next month to fill complete grid (multiples of 7)
        val remainder = cells.size % 7
        if (remainder != 0) {
            val trailingDaysCount = 7 - remainder
            val nextMonth = month.next()
            val nextYear = if (nextMonth == BanglaMonth.BOISHAKH) year + 1 else year
            val firstNextGregorian = toGregorian(BanglaDate(nextYear, nextMonth, 1))

            for (d in 1..trailingDaysCount) {
                val bDate = BanglaDate(nextYear, nextMonth, d)
                val gDate = firstNextGregorian.plusDays((d - 1).toLong())
                val dow = BanglaDayOfWeek.fromJavaDayOfWeek(gDate.dayOfWeek)
                cells.add(
                    CalendarDayCell(
                        banglaDate = bDate,
                        gregorianDate = gDate,
                        dayOfWeek = dow,
                        isCurrentMonth = false,
                        isToday = bDate == today,
                        isSelected = bDate == selectedDate,
                        isMonthEndDay = bDate.day == 29,
                        hasEventOrNote = false
                    )
                )
            }
        }

        return cells
    }
}
