package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.BanglaDate
import com.example.calendar.BanglaMonth
import com.example.calendar.BanglaSeason
import com.example.calendar.toBangla
import com.example.ui.theme.OneUiBlue
import com.example.ui.theme.OneUiCardVariant
import com.example.ui.theme.OneUiTextPrimary
import com.example.ui.theme.OneUiTextSecondary
import com.example.ui.theme.OneUiTextTertiary

@Composable
fun OneUiTopBar(
    displayedYear: Int,
    displayedMonth: BanglaMonth,
    gregorianMonthSpan: String,
    selectedDate: BanglaDate,
    today: BanglaDate,
    isNotificationActive: Boolean,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onTodayClick: () -> Unit,
    onJumpToDateClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onGuideClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isShowingTodayMonth = displayedYear == today.year && displayedMonth == today.month

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        // Top action icons row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Official App branding tag: BanglaCalNDR
            Surface(
                color = OneUiCardVariant,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(OneUiBlue, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "BanglaCalNDR",
                        style = MaterialTheme.typography.labelMedium,
                        color = OneUiTextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Quick actions
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Today button pill with smooth animated reveal
                AnimatedVisibility(
                    visible = !isShowingTodayMonth || selectedDate != today,
                    enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)),
                    exit = fadeOut(spring(stiffness = Spring.StiffnessMediumLow))
                ) {
                    Surface(
                        color = OneUiBlue.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .clickable(onClick = onTodayClick)
                            .testTag("today_button")
                    ) {
                        Text(
                            text = "আজ",
                            style = MaterialTheme.typography.labelMedium,
                            color = OneUiBlue,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                IconButton(
                    onClick = onJumpToDateClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("jump_date_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "তারিখ নির্বাচন",
                        tint = OneUiTextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("notification_button")
                ) {
                    Icon(
                        imageVector = if (isNotificationActive) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                        contentDescription = "মাস সমাপ্তি বিজ্ঞপ্তি",
                        tint = if (isNotificationActive) OneUiBlue else OneUiTextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(
                    onClick = onGuideClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("guide_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "বাংলা মাস ও ঋতু পরিচিতি",
                        tint = OneUiTextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Samsung One UI signature large Viewing Area header:
        // Bangla Month & Year prominent, Gregorian span subtle underneath
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Primary Bangla Month (Large typography)
                Text(
                    text = displayedMonth.banglaName,
                    style = MaterialTheme.typography.displayMedium,
                    color = OneUiTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))

                // Bangla Year & Season
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${displayedYear.toBangla()} বঙ্গাব্দ",
                        style = MaterialTheme.typography.titleMedium,
                        color = OneUiTextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "•",
                        color = OneUiTextTertiary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = BanglaSeason.forMonth(displayedMonth).banglaName,
                        style = MaterialTheme.typography.titleMedium,
                        color = OneUiBlue
                    )
                }

                // Subtle Gregorian month and year span beneath
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = gregorianMonthSpan,
                    style = MaterialTheme.typography.bodySmall,
                    color = OneUiTextTertiary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            // Month navigation arrows
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(OneUiCardVariant)
                        .clickable(onClick = onPreviousMonth)
                        .testTag("prev_month_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "পূর্ববর্তী মাস",
                        tint = OneUiTextPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(OneUiCardVariant)
                        .clickable(onClick = onNextMonth)
                        .testTag("next_month_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "পরবর্তী মাস",
                        tint = OneUiTextPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
