package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calendar.BanglaCalendarEngine
import com.example.calendar.BanglaDate
import com.example.ui.components.AddNoteDialog
import com.example.ui.components.JumpToDateDialog
import com.example.ui.components.MonthGuideDialog
import com.example.ui.components.OneUiCalendarGrid
import com.example.ui.components.OneUiNotificationDialog
import com.example.ui.components.OneUiSelectedDateCard
import com.example.ui.components.OneUiTopBar
import com.example.ui.theme.OneUiBlack
import kotlinx.coroutines.launch

@Composable
fun BanglaCalendarScreen(
    viewModel: BanglaCalendarViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var showJumpDialog by remember { mutableStateOf(false) }
    var showNotificationDialog by remember { mutableStateOf(false) }
    var showAddNoteDialog by remember { mutableStateOf(false) }
    var showGuideDialog by remember { mutableStateOf(false) }

    // Natural fluid spring animation specification for One UI / iOS feel
    val smoothSpringSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    // Hardware-accelerated Horizontal Pager for buttery-smooth month swiping
    val initialPage = remember {
        BanglaCalendarEngine.yearMonthToPage(viewModel.todayDate.year, viewModel.todayDate.month)
    }
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { BanglaCalendarEngine.TOTAL_PAGES }
    )

    // Sync Pager swipes with ViewModel state
    LaunchedEffect(pagerState.currentPage) {
        viewModel.onPageChanged(pagerState.currentPage)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OneUiBlack,
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(OneUiBlack)
                .verticalScroll(scrollState)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            // Samsung One UI Top Viewing Header with Dual Calendar
            OneUiTopBar(
                displayedYear = uiState.selectedYear,
                displayedMonth = uiState.selectedMonth,
                gregorianMonthSpan = uiState.gregorianMonthSpan,
                selectedDate = uiState.selectedDate,
                today = uiState.today,
                isNotificationActive = uiState.isNotificationEnabled,
                onPreviousMonth = {
                    coroutineScope.launch {
                        val prevPage = pagerState.currentPage - 1
                        if (prevPage >= 0) {
                            pagerState.animateScrollToPage(prevPage, animationSpec = smoothSpringSpec)
                        }
                    }
                },
                onNextMonth = {
                    coroutineScope.launch {
                        val nextPage = pagerState.currentPage + 1
                        if (nextPage < BanglaCalendarEngine.TOTAL_PAGES) {
                            pagerState.animateScrollToPage(nextPage, animationSpec = smoothSpringSpec)
                        }
                    }
                },
                onTodayClick = {
                    coroutineScope.launch {
                        val todayPage = viewModel.jumpToToday()
                        pagerState.animateScrollToPage(todayPage, animationSpec = smoothSpringSpec)
                    }
                },
                onJumpToDateClick = { showJumpDialog = true },
                onNotificationClick = { showNotificationDialog = true },
                onGuideClick = { showGuideDialog = true }
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Buttery-smooth HorizontalPager with swipe gestures & cached adjacent pages
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                beyondViewportPageCount = 1,
                key = { page -> page }
            ) { page ->
                val (pageYear, pageMonth) = remember(page) {
                    BanglaCalendarEngine.pageToYearMonth(page)
                }
                val cells = remember(
                    pageYear,
                    pageMonth,
                    uiState.selectedDate,
                    uiState.today,
                    uiState.notedDaysInMonth
                ) {
                    BanglaCalendarEngine.getMonthGrid(
                        year = pageYear,
                        month = pageMonth,
                        selectedDate = uiState.selectedDate,
                        today = uiState.today,
                        notedDays = if (pageYear == uiState.selectedYear && pageMonth == uiState.selectedMonth) {
                            uiState.notedDaysInMonth
                        } else {
                            emptySet()
                        }
                    )
                }

                OneUiCalendarGrid(
                    cells = cells,
                    onCellClick = { cell ->
                        viewModel.selectDate(cell)
                        // If user clicked a day from next/prev month, smoothly scroll to that page
                        if (!cell.isCurrentMonth) {
                            val targetPage = BanglaCalendarEngine.yearMonthToPage(
                                cell.banglaDate.year,
                                cell.banglaDate.month
                            )
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(targetPage, animationSpec = smoothSpringSpec)
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Animated Selected Date Details & Reminders Focus Block
            AnimatedContent(
                targetState = uiState.selectedDate,
                transitionSpec = {
                    (fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                            slideInVertically(spring(stiffness = Spring.StiffnessMediumLow)) { it / 6 })
                        .togetherWith(
                            fadeOut(spring(stiffness = Spring.StiffnessMediumLow)) +
                                    slideOutVertically(spring(stiffness = Spring.StiffnessMediumLow)) { -it / 6 }
                        )
                },
                label = "selectedDateCardAnim"
            ) { targetDate ->
                OneUiSelectedDateCard(
                    selectedDate = targetDate,
                    today = uiState.today,
                    notes = uiState.notes,
                    onAddNoteClick = { showAddNoteDialog = true },
                    onDeleteNoteClick = { note ->
                        viewModel.deleteNote(note)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("নোট মুছে ফেলা হয়েছে")
                        }
                    },
                    onJumpTo29th = {
                        val d29 = BanglaDate(uiState.selectedYear, uiState.selectedMonth, 29)
                        viewModel.selectDate(d29)
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Dialogs
    if (showJumpDialog) {
        JumpToDateDialog(
            initialYear = uiState.selectedYear,
            initialMonth = uiState.selectedMonth,
            onConfirm = { year, month ->
                val targetPage = viewModel.jumpToMonthYear(year, month)
                coroutineScope.launch {
                    pagerState.animateScrollToPage(targetPage, animationSpec = smoothSpringSpec)
                }
            },
            onDismiss = { showJumpDialog = false }
        )
    }

    if (showNotificationDialog) {
        OneUiNotificationDialog(
            isEnabled = uiState.isNotificationEnabled,
            notificationHour = uiState.notificationHour,
            notificationMinute = uiState.notificationMinute,
            nextScheduledDate = uiState.nextScheduledDate,
            onToggleEnabled = { enabled ->
                viewModel.toggleNotification(enabled)
            },
            onTestNotification = {
                viewModel.triggerTestNotification()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("পরীক্ষামূলক বিজ্ঞপ্তি পাঠানো হয়েছে!")
                }
            },
            onDismiss = { showNotificationDialog = false }
        )
    }

    if (showAddNoteDialog) {
        AddNoteDialog(
            selectedDate = uiState.selectedDate,
            onSaveNote = { title, desc ->
                viewModel.addNote(title, desc)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("নোট সংরক্ষিত হয়েছে")
                }
            },
            onDismiss = { showAddNoteDialog = false }
        )
    }

    if (showGuideDialog) {
        MonthGuideDialog(
            onDismiss = { showGuideDialog = false }
        )
    }
}
