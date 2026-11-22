package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.BanglaDayOfWeek
import com.example.calendar.CalendarDayCell
import com.example.calendar.toBangla
import com.example.ui.theme.OneUiAlertAmber
import com.example.ui.theme.OneUiBlue
import com.example.ui.theme.OneUiBorder
import com.example.ui.theme.OneUiCardDark
import com.example.ui.theme.OneUiFridayRed
import com.example.ui.theme.OneUiSaturdayBlue
import com.example.ui.theme.OneUiTextDisabled
import com.example.ui.theme.OneUiTextPrimary
import com.example.ui.theme.OneUiTextSecondary
import com.example.ui.theme.OneUiTextTertiary

@Composable
fun OneUiCalendarGrid(
    cells: List<CalendarDayCell>,
    onCellClick: (CalendarDayCell) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = OneUiCardDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(OneUiBorder))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 16.dp)
        ) {
            // Days of the week header row (রবি - শনি)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val weekdays = listOf(
                    BanglaDayOfWeek.SUNDAY,
                    BanglaDayOfWeek.MONDAY,
                    BanglaDayOfWeek.TUESDAY,
                    BanglaDayOfWeek.WEDNESDAY,
                    BanglaDayOfWeek.THURSDAY,
                    BanglaDayOfWeek.FRIDAY,
                    BanglaDayOfWeek.SATURDAY
                )

                for (dow in weekdays) {
                    val textColor = when (dow) {
                        BanglaDayOfWeek.FRIDAY -> OneUiFridayRed
                        BanglaDayOfWeek.SATURDAY -> OneUiSaturdayBlue
                        else -> OneUiTextSecondary
                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dow.shortName,
                            style = MaterialTheme.typography.labelMedium,
                            color = textColor,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Calendar month grid rows (chunks of 7)
            val rows = cells.chunked(7)
            for (row in rows) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (cell in row) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CalendarDayCellItem(
                                cell = cell,
                                onClick = { onCellClick(cell) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCellItem(
    cell: CalendarDayCell,
    onClick: () -> Unit
) {
    val isSelected = cell.isSelected
    val isToday = cell.isToday
    val isCurrentMonth = cell.isCurrentMonth
    val isMonthEnd = cell.isMonthEndDay && isCurrentMonth

    // Smooth spring scale animation on selection
    val scaleAnim by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "cellScale"
    )

    val animatedBgColor by animateColorAsState(
        targetValue = when {
            isSelected -> OneUiBlue
            else -> Color.Transparent
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "cellBgAnim"
    )

    // Primary Bangla numeral color
    val banglaTextColor = when {
        isSelected -> Color.White
        !isCurrentMonth -> OneUiTextDisabled
        cell.dayOfWeek == BanglaDayOfWeek.FRIDAY -> OneUiFridayRed
        cell.dayOfWeek == BanglaDayOfWeek.SATURDAY -> OneUiSaturdayBlue
        isMonthEnd -> OneUiAlertAmber
        else -> OneUiTextPrimary
    }

    // Secondary Gregorian numeral color (subtle, clean, never dominating)
    val gregorianTextColor = when {
        isSelected -> Color.White.copy(alpha = 0.85f)
        !isCurrentMonth -> OneUiTextDisabled.copy(alpha = 0.5f)
        isToday -> OneUiBlue.copy(alpha = 0.9f)
        cell.dayOfWeek == BanglaDayOfWeek.FRIDAY -> OneUiFridayRed.copy(alpha = 0.65f)
        cell.dayOfWeek == BanglaDayOfWeek.SATURDAY -> OneUiSaturdayBlue.copy(alpha = 0.65f)
        else -> OneUiTextTertiary
    }

    Box(
        modifier = Modifier
            .size(46.dp)
            .scale(scaleAnim)
            .clip(CircleShape)
            .background(animatedBgColor)
            .then(
                if (isToday && !isSelected) {
                    Modifier.border(1.5.dp, OneUiBlue, CircleShape)
                } else if (isMonthEnd && !isSelected) {
                    Modifier.border(1.dp, OneUiAlertAmber.copy(alpha = 0.5f), CircleShape)
                } else {
                    Modifier
                }
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = OneUiBlue),
                onClick = onClick
            )
            .testTag("day_cell_${cell.banglaDate.day}_${cell.banglaDate.month.monthIndex}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 1. Primary Bangla numeral (Prominent)
            Text(
                text = cell.banglaDate.day.toBangla(),
                style = MaterialTheme.typography.titleMedium,
                color = banglaTextColor,
                fontWeight = if (isSelected || isToday || isMonthEnd) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 15.sp,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center
            )

            // 2. Secondary Gregorian numeral (Subtle beneath)
            Text(
                text = cell.gregorianDayText,
                fontFamily = FontFamily.Default,
                style = MaterialTheme.typography.labelSmall,
                color = gregorianTextColor,
                fontSize = 10.sp,
                lineHeight = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            // 3. Indicator dot for events / 29th month-end alert
            if (cell.hasEventOrNote && isCurrentMonth) {
                Spacer(modifier = Modifier.height(1.dp))
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(if (isSelected) Color.White else OneUiBlue, CircleShape)
                )
            } else if (isMonthEnd && !isSelected) {
                Spacer(modifier = Modifier.height(1.dp))
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(OneUiAlertAmber, CircleShape)
                )
            }
        }
    }
}
