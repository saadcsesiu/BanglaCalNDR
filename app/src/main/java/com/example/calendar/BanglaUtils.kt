package com.example.calendar

/**
 * Utility functions for Bengali numeral formatting and strings.
 */
object BanglaUtils {
    private val BANGLA_DIGITS = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

    fun toBangla(number: Int): String {
        return toBangla(number.toLong())
    }

    fun toBangla(number: Long): String {
        val s = number.toString()
        val sb = StringBuilder(s.length)
        for (c in s) {
            if (c in '0'..'9') {
                sb.append(BANGLA_DIGITS[c - '0'])
            } else {
                sb.append(c)
            }
        }
        return sb.toString()
    }

    fun convertDigitsToBangla(text: String): String {
        val sb = StringBuilder(text.length)
        for (c in text) {
            if (c in '0'..'9') {
                sb.append(BANGLA_DIGITS[c - '0'])
            } else {
                sb.append(c)
            }
        }
        return sb.toString()
    }
}

fun Int.toBangla(): String = BanglaUtils.toBangla(this)
fun Long.toBangla(): String = BanglaUtils.toBangla(this)
fun String.toBanglaDigits(): String = BanglaUtils.convertDigitsToBangla(this)
