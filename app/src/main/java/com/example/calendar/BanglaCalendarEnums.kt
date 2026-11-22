package com.example.calendar

/**
 * 12 months of the Bengali Calendar (বঙ্গাব্দ)
 */
enum class BanglaMonth(
    val monthIndex: Int, // 1 to 12
    val banglaName: String,
    val banglaShortName: String
) {
    BOISHAKH(1, "বৈশাখ", "বৈশাখ"),
    JAISHTHA(2, "জ্যৈষ্ঠ", "জ্যৈষ্ঠ"),
    ASHARH(3, "আষাঢ়", "আষাঢ়"),
    SHRABON(4, "শ্রাবণ", "শ্রাবণ"),
    BHADRO(5, "ভাদ্র", "ভাদ্র"),
    ASHWIN(6, "আশ্বিন", "আশ্বিন"),
    KARTIK(7, "কার্তিক", "কার্তিক"),
    AGRAHAYAN(8, "অগ্রহায়ণ", "অগ্রহায়ণ"),
    POUSH(9, "পৌষ", "পৌষ"),
    MAGH(10, "মাঘ", "মাঘ"),
    FALGUN(11, "ফাল্গুন", "ফাল্গুন"),
    CHOITRA(12, "চৈত্র", "চৈত্র");

    fun next(): BanglaMonth {
        return if (this == CHOITRA) BOISHAKH else values()[ordinal + 1]
    }

    fun previous(): BanglaMonth {
        return if (this == BOISHAKH) CHOITRA else values()[ordinal - 1]
    }

    companion object {
        fun fromIndex(index: Int): BanglaMonth {
            return values().firstOrNull { it.monthIndex == index } ?: BOISHAKH
        }
    }
}

/**
 * 6 Bengali Seasons (ষড়ঋতু)
 */
enum class BanglaSeason(
    val banglaName: String,
    val description: String,
    val monthsText: String
) {
    GRISHMA("গ্রীষ্মকাল", "প্রচণ্ড গরম ও কালবৈশাখীর ঋতু", "বৈশাখ ও জ্যৈষ্ঠ"),
    BARSHA("বর্ষাকাল", "অঝোর ধারায় বৃষ্টি ও সতেজতার ঋতু", "আষাঢ় ও শ্রাবণ"),
    SHARAT("শরৎকাল", "নীল আকাশ ও শুভ্র কাশফুলের ঋতু", "ভাদ্র ও আশ্বিন"),
    HEMANTA("হেমন্তকাল", "নতুন ধান ও নবান্ন উৎসবের ঋতু", "কার্তিক ও অগ্রহায়ণ"),
    SHEET("শীতকাল", "কুয়াশাচ্ছন্ন সকাল ও পিঠা-পুলির ঋতু", "পৌষ ও মাঘ"),
    BASANTA("বসন্তকাল", "ঋতুরাজ, ফুলে ফুলে রঙিন প্রকৃতির ঋতু", "ফাল্গুন ও চৈত্র");

    companion object {
        fun forMonth(month: BanglaMonth): BanglaSeason {
            return when (month) {
                BanglaMonth.BOISHAKH, BanglaMonth.JAISHTHA -> GRISHMA
                BanglaMonth.ASHARH, BanglaMonth.SHRABON -> BARSHA
                BanglaMonth.BHADRO, BanglaMonth.ASHWIN -> SHARAT
                BanglaMonth.KARTIK, BanglaMonth.AGRAHAYAN -> HEMANTA
                BanglaMonth.POUSH, BanglaMonth.MAGH -> SHEET
                BanglaMonth.FALGUN, BanglaMonth.CHOITRA -> BASANTA
            }
        }
    }
}

/**
 * Days of the week in Bengali
 */
enum class BanglaDayOfWeek(
    val dayIndex: Int, // 1=Sunday, 2=Monday, ... 7=Saturday
    val fullName: String,
    val shortName: String,
    val isWeekend: Boolean
) {
    SUNDAY(1, "রবিবার", "রবি", false),
    MONDAY(2, "সোমবার", "সোম", false),
    TUESDAY(3, "মঙ্গলবার", "মঙ্গল", false),
    WEDNESDAY(4, "বুধবার", "বুধ", false),
    THURSDAY(5, "বৃহস্পতিবার", "বৃহঃ", false),
    FRIDAY(6, "শুক্রবার", "শুক্র", true),
    SATURDAY(7, "শনিবার", "শনি", true);

    companion object {
        fun fromJavaDayOfWeek(dayOfWeek: java.time.DayOfWeek): BanglaDayOfWeek {
            return when (dayOfWeek) {
                java.time.DayOfWeek.SUNDAY -> SUNDAY
                java.time.DayOfWeek.MONDAY -> MONDAY
                java.time.DayOfWeek.TUESDAY -> TUESDAY
                java.time.DayOfWeek.WEDNESDAY -> WEDNESDAY
                java.time.DayOfWeek.THURSDAY -> THURSDAY
                java.time.DayOfWeek.FRIDAY -> FRIDAY
                java.time.DayOfWeek.SATURDAY -> SATURDAY
            }
        }
    }
}
