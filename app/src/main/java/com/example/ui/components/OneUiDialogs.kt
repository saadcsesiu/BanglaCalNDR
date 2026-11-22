package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.calendar.BanglaDate
import com.example.calendar.BanglaMonth
import com.example.calendar.BanglaSeason
import com.example.calendar.toBangla
import com.example.ui.theme.OneUiBlue
import com.example.ui.theme.OneUiBorder
import com.example.ui.theme.OneUiCardDark
import com.example.ui.theme.OneUiCardVariant
import com.example.ui.theme.OneUiTextPrimary
import com.example.ui.theme.OneUiTextSecondary
import com.example.ui.theme.OneUiTextTertiary

@Composable
fun JumpToDateDialog(
    initialYear: Int,
    initialMonth: BanglaMonth,
    onConfirm: (year: Int, month: BanglaMonth) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedYear by remember { mutableStateOf(initialYear) }
    var selectedMonth by remember { mutableStateOf(initialMonth) }

    val years = remember(initialYear) {
        (initialYear - 5..initialYear + 5).toList()
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = OneUiCardDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Title & Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "তারিখে যান",
                        style = MaterialTheme.typography.titleLarge,
                        color = OneUiTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "বাতিল",
                            tint = OneUiTextTertiary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Year selector
                Text(
                    text = "সাল নির্বাচন (বঙ্গাব্দ)",
                    style = MaterialTheme.typography.labelLarge,
                    color = OneUiTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(years) { y ->
                        val isYearSelected = y == selectedYear
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isYearSelected) OneUiBlue else OneUiCardVariant,
                            modifier = Modifier.clickable { selectedYear = y }
                        ) {
                            Text(
                                text = "${y.toBangla()} বঙ্গাব্দ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isYearSelected) Color.White else OneUiTextPrimary,
                                fontWeight = if (isYearSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Month Grid (12 Bangla months)
                Text(
                    text = "মাস নির্বাচন",
                    style = MaterialTheme.typography.labelLarge,
                    color = OneUiTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                val monthRows = BanglaMonth.values().toList().chunked(3)
                for (row in monthRows) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (m in row) {
                            val isMonthSelected = m == selectedMonth
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isMonthSelected) OneUiBlue else OneUiCardVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedMonth = m }
                            ) {
                                Text(
                                    text = m.banglaName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isMonthSelected) Color.White else OneUiTextPrimary,
                                    fontWeight = if (isMonthSelected) FontWeight.Bold else FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OneUiTextSecondary)
                    ) {
                        Text("বাতিল")
                    }

                    Button(
                        onClick = {
                            onConfirm(selectedYear, selectedMonth)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OneUiBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text("যাও", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddNoteDialog(
    selectedDate: BanglaDate,
    onSaveNote: (title: String, description: String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = OneUiCardDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "স্মরণিকা ও নোট যোগ",
                            style = MaterialTheme.typography.titleLarge,
                            color = OneUiTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${selectedDate.day.toBangla()} ${selectedDate.month.banglaName} ${selectedDate.year.toBangla()} বঙ্গাব্দ",
                            style = MaterialTheme.typography.bodySmall,
                            color = OneUiBlue
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "বাতিল",
                            tint = OneUiTextTertiary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("নোটের শিরোনাম", color = OneUiTextSecondary) },
                    placeholder = { Text("যেমন: ভাড়ার হিসাব, অনুষ্ঠান বা কাজ", color = OneUiTextTertiary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OneUiBlue,
                        unfocusedBorderColor = OneUiBorder,
                        focusedTextColor = OneUiTextPrimary,
                        unfocusedTextColor = OneUiTextPrimary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("note_title_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("বিস্তারিত বিবরণ (ঐচ্ছিক)", color = OneUiTextSecondary) },
                    minLines = 3,
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OneUiBlue,
                        unfocusedBorderColor = OneUiBorder,
                        focusedTextColor = OneUiTextPrimary,
                        unfocusedTextColor = OneUiTextPrimary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("note_desc_input")
                )

                Spacer(modifier = Modifier.height(22.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OneUiTextSecondary)
                    ) {
                        Text("বাতিল")
                    }

                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSaveNote(title, description)
                                onDismiss()
                            }
                        },
                        enabled = title.isNotBlank(),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("save_note_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OneUiBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text("সংরক্ষণ", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun MonthGuideDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = OneUiCardDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "বাংলা ১২ মাস ও ঋতু পরিচিতি",
                            style = MaterialTheme.typography.titleLarge,
                            color = OneUiTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "বাংলা একাডেমি প্রমিত দিনপঞ্জিকা",
                            style = MaterialTheme.typography.labelMedium,
                            color = OneUiTextSecondary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "বন্ধ করুন",
                            tint = OneUiTextTertiary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.height(340.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(BanglaSeason.values()) { season ->
                        Surface(
                            color = OneUiCardVariant,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = season.banglaName,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = OneUiBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = season.monthsText,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = OneUiTextSecondary
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = season.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OneUiTextPrimary
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = OneUiCardVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "মাসসমূহের দিন গণনা নিয়ম:",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = OneUiTextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• বৈশাখ থেকে আশ্বিন: প্রতি মাস ৩১ দিন (প্রথম ৬ মাস)\n• কার্তিক থেকে মাঘ: প্রতি মাস ৩০ দিন (পরের ৪ মাস)\n• ফাল্গুন: ২৯ দিন (অধিবর্ষে ৩০ দিন)\n• চৈত্র: ৩০ দিন",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OneUiTextSecondary,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OneUiBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text("ঠিক আছে", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
