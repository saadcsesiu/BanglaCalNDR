package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.BanglaCalendarEngine
import com.example.calendar.BanglaDate
import com.example.calendar.toBangla
import com.example.data.BanglaNote
import com.example.ui.theme.OneUiAlertAmber
import com.example.ui.theme.OneUiBlue
import com.example.ui.theme.OneUiBorder
import com.example.ui.theme.OneUiCardDark
import com.example.ui.theme.OneUiCardVariant
import com.example.ui.theme.OneUiFridayRed
import com.example.ui.theme.OneUiTextPrimary
import com.example.ui.theme.OneUiTextSecondary
import com.example.ui.theme.OneUiTextTertiary

@Composable
fun OneUiSelectedDateCard(
    selectedDate: BanglaDate,
    today: BanglaDate,
    notes: List<BanglaNote>,
    onAddNoteClick: () -> Unit,
    onDeleteNoteClick: (BanglaNote) -> Unit,
    onJumpTo29th: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dayOfWeek = BanglaCalendarEngine.getDayOfWeek(selectedDate)
    val daysRemaining = BanglaCalendarEngine.getDaysRemainingInMonth(selectedDate)
    val isToday = selectedDate == today
    val is29th = selectedDate.day == 29

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = OneUiCardDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(OneUiBorder))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Row: Full Date and Day of Week
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Primary Bangla Date
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${selectedDate.day.toBangla()} ${selectedDate.month.banglaName}",
                            style = MaterialTheme.typography.displaySmall,
                            color = OneUiTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        if (isToday) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = OneUiBlue.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "আজ",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = OneUiBlue,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Bangla weekday and year
                    Text(
                        text = "${dayOfWeek.fullName} • ${selectedDate.year.toBangla()} বঙ্গাব্দ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (dayOfWeek.isWeekend) OneUiFridayRed else OneUiTextSecondary,
                        fontWeight = FontWeight.Medium
                    )

                    // Secondary Gregorian Date subtitle (clean, subtle, never dominating)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = selectedDate.toGregorianFormatted(),
                        fontFamily = FontFamily.Default,
                        style = MaterialTheme.typography.bodySmall,
                        color = OneUiTextTertiary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                // Quick add note button
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(OneUiCardVariant)
                        .clickable(onClick = onAddNoteClick)
                        .testTag("add_note_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "নোট বা রিমাইন্ডার যোগ করুন",
                        tint = OneUiBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Month status chip / info
            if (is29th) {
                Surface(
                    color = OneUiAlertAmber.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = null,
                            tint = OneUiAlertAmber,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "মাস সমাপ্তির বিশেষ দিন (২৯ তারিখ)",
                                style = MaterialTheme.typography.titleMedium,
                                color = OneUiAlertAmber,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "এই দিনে স্বয়ংক্রিয়ভাবে পরবর্তী মাসের আগমনী বার্তা পাঠানো হয়।",
                                style = MaterialTheme.typography.labelMedium,
                                color = OneUiTextPrimary.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Days left chip
                    Surface(
                        color = OneUiCardVariant,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = null,
                                tint = OneUiBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "মাসের বাকি",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = OneUiTextTertiary
                                )
                                Text(
                                    text = if (daysRemaining > 0) "${daysRemaining.toBangla()} দিন" else "শেষ দিন",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = OneUiTextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Season chip
                    Surface(
                        color = OneUiCardVariant,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(OneUiBlue, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "ঋতু",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = OneUiTextTertiary
                                )
                                Text(
                                    text = selectedDate.season.banglaName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = OneUiTextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Notes / Reminders list for this date
            Text(
                text = "দিনের স্মরণিকা ও নোট",
                style = MaterialTheme.typography.titleMedium,
                color = OneUiTextSecondary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (notes.isEmpty()) {
                Surface(
                    color = OneUiCardVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.EventNote,
                            contentDescription = null,
                            tint = OneUiTextTertiary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "এই দিনে কোন নোট নেই। যোগ করতে '+' চাপুন।",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OneUiTextTertiary
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (note in notes) {
                        NoteItemRow(
                            note = note,
                            onDelete = { onDeleteNoteClick(note) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NoteItemRow(
    note: BanglaNote,
    onDelete: () -> Unit
) {
    Surface(
        color = OneUiCardVariant,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = OneUiTextPrimary,
                    fontWeight = FontWeight.Medium
                )
                if (note.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = note.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = OneUiTextSecondary
                    )
                }
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "নোট মুছে ফেলুন",
                    tint = OneUiTextTertiary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
