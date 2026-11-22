package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.notification.NotificationHelper
import com.example.ui.BanglaCalendarScreen
import com.example.ui.BanglaCalendarViewModel
import com.example.ui.theme.BanglaCalendarTheme

class MainActivity : ComponentActivity() {

    private val calendarViewModel: BanglaCalendarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ensure notification channel is created
        NotificationHelper.createNotificationChannel(this)

        setContent {
            BanglaCalendarTheme {
                BanglaCalendarScreen(viewModel = calendarViewModel)
            }
        }
    }
}
