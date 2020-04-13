package com.chococard.carwash.util.extension

import android.app.Activity
import android.app.DatePickerDialog
import java.util.*

fun Activity.dialogDatePicker(date: (Int, Int, Int) -> Unit) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        this,
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            date.invoke(year, month + 1, dayOfMonth)
        },
        year,
        month,
        day
    )
    dpd.show()
}