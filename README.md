# BanglaCalNDR 📅

<p align="center">
  <img src="app/src/main/res/drawable/ic_banglacalndr_icon_1792041734537.jpg" width="128" height="128" alt="BanglaCalNDR Icon" style="border-radius: 28px;" />
</p>

<p align="center">
  <b>A lightweight, ultra-low battery consuming Bangla-First Dual Calendar designed to look and feel like the Samsung One UI Calendar in Default Dark Mode.</b>
</p>

---

## ✨ Features

- **Samsung One UI Default Dark Mode**: AMOLED black background (`#000000`) with signature One UI rounded focus blocks (`#161619` with `26.dp` corners).
- **Bangla-First Dual Calendar**:
  - **Primary**: Bangla month names (`আশ্বিন`), Bangla year (`১৪৩৩ বঙ্গাব্দ`), Bengali season indicators (`শরৎকাল`), and prominent Bengali digits (`১ - ৩১`).
  - **Secondary**: Subtle English Gregorian date (`1 - 31`) displayed underneath each Bangla date cell in smaller Latin numerals (`10.sp`) without cluttering the UI.
  - English month and year span (e.g. `September – October 2026`) displayed as a clean subtitle.
- **Buttery-Smooth Horizontal Pager**:
  - Horizontal swipe gesture physics between months with momentum and spring bounce.
  - Pre-cached adjacent pages for 60+ FPS performance with zero dropped frames.
- **End-of-Month Notification (29th Day)**:
  - Battery-friendly local alarm via `AlarmManager` triggering on the 29th of every Bangla month.
  - Bangla notification title and body alerting about the upcoming month transition.
  - Auto-reschedules across device reboots (`RECEIVE_BOOT_COMPLETED`).
- **100% Offline & Private**:
  - Local Room SQLite database for personal notes/reminders tied to specific Bangla dates.
  - Zero network calls, zero tracking, zero background battery consumption.
- **Pure Bangla Academy Standard**:
  - 1st Boishakh = April 14.
  - First 6 months (Boishakh to Ashwin) = 31 days each.
  - Next 4 months (Kartik to Magh) = 30 days each.
  - Falgun = 29 days (30 days in leap year).
  - Choitra = 30 days.

---

## 📲 How to Download & Install the APK

1. Go to the **[Releases](../../releases)** tab on GitHub.
2. Download the latest `BanglaCalNDR.apk` asset.
3. Open the APK on your Android device and tap **Install**.

---

## 🛠️ Building from Source

Prerequisites:
- Android Studio Ladybug / Meerkat or JDK 17+
- Android SDK 36 (minSdk: 24)


## 📜 License
Open source and lightweight.
