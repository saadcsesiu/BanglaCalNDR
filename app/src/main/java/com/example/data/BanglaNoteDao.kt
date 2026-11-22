package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BanglaNoteDao {

    @Query("SELECT * FROM bangla_notes WHERE year = :year AND monthIndex = :monthIndex AND day = :day ORDER BY id DESC")
    fun getNotesForDate(year: Int, monthIndex: Int, day: Int): Flow<List<BanglaNote>>

    @Query("SELECT DISTINCT day FROM bangla_notes WHERE year = :year AND monthIndex = :monthIndex")
    fun getNotedDaysInMonth(year: Int, monthIndex: Int): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: BanglaNote): Long

    @Delete
    suspend fun deleteNote(note: BanglaNote)

    @Query("DELETE FROM bangla_notes WHERE id = :id")
    suspend fun deleteNoteById(id: Long)
}
