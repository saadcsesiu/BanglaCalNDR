package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.calendar.BanglaDate
import com.example.calendar.BanglaMonth

@Entity(tableName = "bangla_notes")
data class BanglaNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val year: Int,
    val monthIndex: Int,
    val day: Int,
    val title: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toBanglaDate(): BanglaDate {
        return BanglaDate(year, BanglaMonth.fromIndex(monthIndex), day)
    }
}
