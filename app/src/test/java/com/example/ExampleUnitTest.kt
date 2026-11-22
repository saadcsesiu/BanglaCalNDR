package com.example

import com.example.calendar.BanglaCalendarEngine
import com.example.calendar.BanglaDate
import com.example.calendar.BanglaMonth
import com.example.calendar.BanglaSeason
import com.example.calendar.toBangla
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ExampleUnitTest {

    @Test
    fun testBanglaNumeralFormatting() {
        assertEquals("০", 0.toBangla())
        assertEquals("১", 1.toBangla())
        assertEquals("৯", 9.toBangla())
        assertEquals("২৯", 29.toBangla())
        assertEquals("১৪৩১", 1431.toBangla())
        assertEquals("১৪৩৩", 1433.toBangla())
    }

    @Test
    fun testPahelaBaishakhTransitions() {
        // Pahela Baishakh across consecutive years must strictly be 1 Boishakh
        val y2023 = BanglaCalendarEngine.fromGregorian(LocalDate.of(2023, 4, 14))
        assertEquals(1430, y2023.year)
        assertEquals(BanglaMonth.BOISHAKH, y2023.month)
        assertEquals(1, y2023.day)

        val y2024 = BanglaCalendarEngine.fromGregorian(LocalDate.of(2024, 4, 14))
        assertEquals(1431, y2024.year)
        assertEquals(BanglaMonth.BOISHAKH, y2024.month)
        assertEquals(1, y2024.day)

        val y2025 = BanglaCalendarEngine.fromGregorian(LocalDate.of(2025, 4, 14))
        assertEquals(1432, y2025.year)
        assertEquals(BanglaMonth.BOISHAKH, y2025.month)
        assertEquals(1, y2025.day)

        val y2026 = BanglaCalendarEngine.fromGregorian(LocalDate.of(2026, 4, 14))
        assertEquals(1433, y2026.year)
        assertEquals(BanglaMonth.BOISHAKH, y2026.month)
        assertEquals(1, y2026.day)
    }

    @Test
    fun testYearEndBoundaries() {
        // 13 April (last day of Bengali year) must strictly be 30 Choitra
        val apr13_2024 = BanglaCalendarEngine.fromGregorian(LocalDate.of(2024, 4, 13))
        assertEquals(1430, apr13_2024.year)
        assertEquals(BanglaMonth.CHOITRA, apr13_2024.month)
        assertEquals(30, apr13_2024.day)

        val apr13_2025 = BanglaCalendarEngine.fromGregorian(LocalDate.of(2025, 4, 13))
        assertEquals(1431, apr13_2025.year)
        assertEquals(BanglaMonth.CHOITRA, apr13_2025.month)
        assertEquals(30, apr13_2025.day)
    }

    @Test
    fun testBanglaMonthBoundaries1431() {
        // Test all month boundaries for Bengali Year 1431
        assertEquals(LocalDate.of(2024, 4, 14), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.BOISHAKH, 1)))
        assertEquals(LocalDate.of(2024, 5, 14), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.BOISHAKH, 31)))

        assertEquals(LocalDate.of(2024, 5, 15), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.JAISHTHA, 1)))
        assertEquals(LocalDate.of(2024, 6, 14), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.JAISHTHA, 31)))

        assertEquals(LocalDate.of(2024, 6, 15), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.ASHARH, 1)))
        assertEquals(LocalDate.of(2024, 7, 15), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.ASHARH, 31)))

        assertEquals(LocalDate.of(2024, 7, 16), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.SHRABON, 1)))
        assertEquals(LocalDate.of(2024, 8, 15), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.SHRABON, 31)))

        assertEquals(LocalDate.of(2024, 8, 16), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.BHADRO, 1)))
        assertEquals(LocalDate.of(2024, 9, 15), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.BHADRO, 31)))

        assertEquals(LocalDate.of(2024, 9, 16), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.ASHWIN, 1)))
        assertEquals(LocalDate.of(2024, 10, 16), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.ASHWIN, 31)))

        assertEquals(LocalDate.of(2024, 10, 17), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.KARTIK, 1)))
        assertEquals(LocalDate.of(2024, 11, 15), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.KARTIK, 30)))

        assertEquals(LocalDate.of(2024, 11, 16), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.AGRAHAYAN, 1)))
        assertEquals(LocalDate.of(2024, 12, 15), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.AGRAHAYAN, 30)))

        assertEquals(LocalDate.of(2024, 12, 16), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.POUSH, 1)))
        assertEquals(LocalDate.of(2025, 1, 14), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.POUSH, 30)))

        assertEquals(LocalDate.of(2025, 1, 15), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.MAGH, 1)))
        assertEquals(LocalDate.of(2025, 2, 13), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.MAGH, 30)))

        assertEquals(LocalDate.of(2025, 2, 14), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.FALGUN, 1)))
        assertEquals(LocalDate.of(2025, 3, 14), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.FALGUN, 29)))

        assertEquals(LocalDate.of(2025, 3, 15), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.CHOITRA, 1)))
        assertEquals(LocalDate.of(2025, 4, 13), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.CHOITRA, 30)))
    }

    @Test
    fun testLeapYearBehavior() {
        // Falgun 1430 occurs in Feb 2024 (Leap Year) -> 30 days
        assertTrue(BanglaCalendarEngine.isLeapYear(1430))
        assertEquals(30, BanglaCalendarEngine.daysInMonth(1430, BanglaMonth.FALGUN))
        assertEquals(LocalDate.of(2024, 3, 14), BanglaCalendarEngine.toGregorian(BanglaDate(1430, BanglaMonth.FALGUN, 30)))
        assertEquals(LocalDate.of(2024, 3, 15), BanglaCalendarEngine.toGregorian(BanglaDate(1430, BanglaMonth.CHOITRA, 1)))

        // Falgun 1431 occurs in Feb 2025 (Common Year) -> 29 days
        assertFalse(BanglaCalendarEngine.isLeapYear(1431))
        assertEquals(29, BanglaCalendarEngine.daysInMonth(1431, BanglaMonth.FALGUN))
        assertEquals(LocalDate.of(2025, 3, 14), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.FALGUN, 29)))
        assertEquals(LocalDate.of(2025, 3, 15), BanglaCalendarEngine.toGregorian(BanglaDate(1431, BanglaMonth.CHOITRA, 1)))
    }

    @Test
    fun testHistoricalNationalDays() {
        // 21 February 2024 = 8 Falgun 1430
        val feb21 = BanglaCalendarEngine.fromGregorian(LocalDate.of(2024, 2, 21))
        assertEquals(1430, feb21.year)
        assertEquals(BanglaMonth.FALGUN, feb21.month)
        assertEquals(8, feb21.day)

        // 26 March 2024 = 12 Choitra 1430
        val mar26 = BanglaCalendarEngine.fromGregorian(LocalDate.of(2024, 3, 26))
        assertEquals(1430, mar26.year)
        assertEquals(BanglaMonth.CHOITRA, mar26.month)
        assertEquals(12, mar26.day)

        // 16 December 2024 = 1 Poush 1431
        val dec16 = BanglaCalendarEngine.fromGregorian(LocalDate.of(2024, 12, 16))
        assertEquals(1431, dec16.year)
        assertEquals(BanglaMonth.POUSH, dec16.month)
        assertEquals(1, dec16.day)
    }

    @Test
    fun testContinuousReversibilityAcrossFullYear() {
        // Ensure every single day of year 1431 maps back and forth with 100% fidelity
        var curDate = LocalDate.of(2024, 4, 14)
        val endDate = LocalDate.of(2025, 4, 13)

        while (!curDate.isAfter(endDate)) {
            val bangla = BanglaCalendarEngine.fromGregorian(curDate)
            val gregorianReversed = BanglaCalendarEngine.toGregorian(bangla)
            assertEquals("Mismatch for $curDate -> $bangla", curDate, gregorianReversed)
            curDate = curDate.plusDays(1)
        }
    }

    @Test
    fun testPagingMath() {
        for (y in 1420..1440) {
            for (m in BanglaMonth.values()) {
                val page = BanglaCalendarEngine.yearMonthToPage(y, m)
                val (revYear, revMonth) = BanglaCalendarEngine.pageToYearMonth(page)
                assertEquals(y, revYear)
                assertEquals(m, revMonth)
            }
        }
    }

    @Test
    fun testSeasons() {
        assertEquals(BanglaSeason.GRISHMA, BanglaSeason.forMonth(BanglaMonth.BOISHAKH))
        assertEquals(BanglaSeason.BARSHA, BanglaSeason.forMonth(BanglaMonth.ASHARH))
        assertEquals(BanglaSeason.SHARAT, BanglaSeason.forMonth(BanglaMonth.ASHWIN))
        assertEquals(BanglaSeason.HEMANTA, BanglaSeason.forMonth(BanglaMonth.KARTIK))
        assertEquals(BanglaSeason.SHEET, BanglaSeason.forMonth(BanglaMonth.POUSH))
        assertEquals(BanglaSeason.BASANTA, BanglaSeason.forMonth(BanglaMonth.FALGUN))
    }
}
