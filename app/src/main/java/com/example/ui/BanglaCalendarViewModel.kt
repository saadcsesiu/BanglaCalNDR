package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendar.BanglaCalendarEngine
import com.example.calendar.BanglaDate
import com.example.calendar.BanglaMonth
import com.example.calendar.CalendarDayCell
import com.example.data.BanglaDatabase
import com.example.data.BanglaNote
import com.example.notification.AlarmScheduler
import com.example.notification.NotificationHelper
import com.example.notification.NotificationPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CalendarUiState(
    val selectedYear: Int,
    val selectedMonth: BanglaMonth,
    val selectedDate: BanglaDate,
    val today: BanglaDate,
    val gridCells: List<CalendarDayCell>,
    val gregorianMonthSpan: String,
    val currentPage: Int,
    val notes: List<BanglaNote>,
    val notedDaysInMonth: Set<Int>,
    val isNotificationEnabled: Boolean,
    val notificationHour: Int,
    val notificationMinute: Int,
    val nextScheduledDate: BanglaDate,
    val daysRemainingInMonth: Int
)

class BanglaCalendarViewModel(application: Application) : AndroidViewModel(application) {

    private val db = BanglaDatabase.getDatabase(application)
    private val noteDao = db.banglaNoteDao()
    private val prefs = NotificationPreferences(application)

    val todayDate = BanglaCalendarEngine.today()

    private val _displayedYear = MutableStateFlow(todayDate.year)
    val displayedYear: StateFlow<Int> = _displayedYear.asStateFlow()

    private val _displayedMonth = MutableStateFlow(todayDate.month)
    val displayedMonth: StateFlow<BanglaMonth> = _displayedMonth.asStateFlow()

    private val _selectedDate = MutableStateFlow(todayDate)
    val selectedDate: StateFlow<BanglaDate> = _selectedDate.asStateFlow()

    private val _isNotificationEnabled = MutableStateFlow(prefs.isNotificationEnabled)
    val isNotificationEnabled: StateFlow<Boolean> = _isNotificationEnabled.asStateFlow()

    private val _notificationHour = MutableStateFlow(prefs.notificationHour)
    private val _notificationMinute = MutableStateFlow(prefs.notificationMinute)

    private val _nextScheduledDate = MutableStateFlow(AlarmScheduler.getNextScheduledBanglaDate(application))
    val nextScheduledDate: StateFlow<BanglaDate> = _nextScheduledDate.asStateFlow()

    // Notes for the selected date
    val notesForSelectedDate: StateFlow<List<BanglaNote>> = _selectedDate.flatMapLatest { date ->
        noteDao.getNotesForDate(date.year, date.month.monthIndex, date.day)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Noted days in the currently displayed month
    private val displayedYearMonth = combine(_displayedYear, _displayedMonth) { year, month ->
        Pair(year, month)
    }

    val notedDaysInMonth: StateFlow<List<Int>> = displayedYearMonth.flatMapLatest { (year, month) ->
        noteDao.getNotedDaysInMonth(year, month.monthIndex)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Combined UI State
    val uiState: StateFlow<CalendarUiState> = combine(
        displayedYearMonth,
        _selectedDate,
        notedDaysInMonth,
        notesForSelectedDate,
        _isNotificationEnabled
    ) { (year, month), selected, notedDays, notes, notifEnabled ->
        val grid = BanglaCalendarEngine.getMonthGrid(
            year = year,
            month = month,
            selectedDate = selected,
            today = todayDate,
            notedDays = notedDays.toSet()
        )
        val daysRemaining = BanglaCalendarEngine.getDaysRemainingInMonth(selected)
        val gregorianSpan = BanglaCalendarEngine.getGregorianMonthSpanText(year, month)
        val page = BanglaCalendarEngine.yearMonthToPage(year, month)

        CalendarUiState(
            selectedYear = year,
            selectedMonth = month,
            selectedDate = selected,
            today = todayDate,
            gridCells = grid,
            gregorianMonthSpan = gregorianSpan,
            currentPage = page,
            notes = notes,
            notedDaysInMonth = notedDays.toSet(),
            isNotificationEnabled = notifEnabled,
            notificationHour = _notificationHour.value,
            notificationMinute = _notificationMinute.value,
            nextScheduledDate = _nextScheduledDate.value,
            daysRemainingInMonth = daysRemaining
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CalendarUiState(
            selectedYear = todayDate.year,
            selectedMonth = todayDate.month,
            selectedDate = todayDate,
            today = todayDate,
            gridCells = emptyList(),
            gregorianMonthSpan = BanglaCalendarEngine.getGregorianMonthSpanText(todayDate.year, todayDate.month),
            currentPage = BanglaCalendarEngine.yearMonthToPage(todayDate.year, todayDate.month),
            notes = emptyList(),
            notedDaysInMonth = emptySet(),
            isNotificationEnabled = prefs.isNotificationEnabled,
            notificationHour = prefs.notificationHour,
            notificationMinute = prefs.notificationMinute,
            nextScheduledDate = todayDate,
            daysRemainingInMonth = BanglaCalendarEngine.getDaysRemainingInMonth(todayDate)
        )
    )

    init {
        // Initialize alarm scheduling
        AlarmScheduler.scheduleNextMonthEndAlarm(application)
        refreshNextScheduled()
    }

    fun onPageChanged(page: Int) {
        val (year, month) = BanglaCalendarEngine.pageToYearMonth(page)
        if (_displayedYear.value != year || _displayedMonth.value != month) {
            _displayedYear.value = year
            _displayedMonth.value = month
        }
    }

    fun selectDate(cell: CalendarDayCell) {
        _selectedDate.value = cell.banglaDate
        if (!cell.isCurrentMonth) {
            _displayedYear.value = cell.banglaDate.year
            _displayedMonth.value = cell.banglaDate.month
        }
    }

    fun selectDate(date: BanglaDate) {
        _selectedDate.value = date
        _displayedYear.value = date.year
        _displayedMonth.value = date.month
    }

    fun goToNextMonth(): Int {
        val next = _displayedMonth.value.next()
        val nextYear = if (next == BanglaMonth.BOISHAKH) _displayedYear.value + 1 else _displayedYear.value
        _displayedYear.value = nextYear
        _displayedMonth.value = next
        return BanglaCalendarEngine.yearMonthToPage(nextYear, next)
    }

    fun goToPreviousMonth(): Int {
        val prev = _displayedMonth.value.previous()
        val prevYear = if (prev == BanglaMonth.CHOITRA) _displayedYear.value - 1 else _displayedYear.value
        _displayedYear.value = prevYear
        _displayedMonth.value = prev
        return BanglaCalendarEngine.yearMonthToPage(prevYear, prev)
    }

    fun jumpToToday(): Int {
        _displayedYear.value = todayDate.year
        _displayedMonth.value = todayDate.month
        _selectedDate.value = todayDate
        return BanglaCalendarEngine.yearMonthToPage(todayDate.year, todayDate.month)
    }

    fun jumpToMonthYear(year: Int, month: BanglaMonth): Int {
        _displayedYear.value = year
        _displayedMonth.value = month
        _selectedDate.value = BanglaDate(year, month, 1)
        return BanglaCalendarEngine.yearMonthToPage(year, month)
    }

    fun addNote(title: String, description: String = "") {
        if (title.isBlank()) return
        viewModelScope.launch {
            val date = _selectedDate.value
            val note = BanglaNote(
                year = date.year,
                monthIndex = date.month.monthIndex,
                day = date.day,
                title = title.trim(),
                description = description.trim()
            )
            noteDao.insertNote(note)
        }
    }

    fun deleteNote(note: BanglaNote) {
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }

    fun toggleNotification(enabled: Boolean) {
        prefs.isNotificationEnabled = enabled
        _isNotificationEnabled.value = enabled
        if (enabled) {
            AlarmScheduler.scheduleNextMonthEndAlarm(getApplication())
        } else {
            AlarmScheduler.cancelAlarm(getApplication())
        }
        refreshNextScheduled()
    }

    fun triggerTestNotification() {
        NotificationHelper.showTestNotification(getApplication())
    }

    private fun refreshNextScheduled() {
        _nextScheduledDate.value = AlarmScheduler.getNextScheduledBanglaDate(getApplication())
    }
}
