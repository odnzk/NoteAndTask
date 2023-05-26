package com.noteapp.feature_todolist.util

import androidx.annotation.StringRes
import com.noteapp.ui.R
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

internal enum class TodoDeadline(@StringRes val titleId: Int) {
    NO_DEADLINE(R.string.todo_setting_deadline_no_deadline),
    TODAY(R.string.todo_setting_deadline_today),
    TOMORROW(R.string.todo_setting_deadline_tomorrow),
    CUSTOM(R.string.todo_setting_deadline_custom)
}

internal enum class TodoReminder(@StringRes val titleId: Int) {
    NO_REMINDER(R.string.todo_setting_reminder_no_reminder),
    LATER_TODAY(R.string.todo_setting_reminder_later_today),
    LATER_TOMORROW(R.string.todo_setting_reminder_later_tomorrow),
    CUSTOM(R.string.todo_setting_reminder_custom)
}

internal enum class TodoPeriodicity(@StringRes val titleId: Int, duration: Duration? = null) {
    NO_PERIOD(R.string.todo_setting_period_no_period, null),
    DAILY(R.string.todo_setting_period_daily, 1.days),
    WEEKLY(R.string.todo_setting_period_weekly, 7.days),
    MONTHLY(R.string.todo_setting_period_monthly, 30.days),
    ANNUALLY(R.string.todo_setting_period_annually, 365.days),
    CUSTOM(R.string.todo_setting_period_custom, null)
}
