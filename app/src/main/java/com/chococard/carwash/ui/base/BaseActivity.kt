package com.chococard.carwash.ui.base

import android.view.Menu
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.chococard.carwash.R

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return true
    }

    fun setToolbar(toolbar: Toolbar) {
        toolbar.title = ""
        setSupportActionBar(toolbar)
    }

    fun dialogContactAdmin(
        @StringRes title: Int = R.string.contact_admin,
        @StringRes message: Int = R.string.contact_admin_message,
        contactAdmin: () -> Unit
    ) = AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(android.R.string.ok) { _, _ ->
            contactAdmin.invoke()
        }
        setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        setCancelable(false)
        show()
    }

    fun dialogLogout(
        @StringRes title: Int = R.string.logout,
        @StringRes message: Int = R.string.do_you_really_want_to_log_out,
        logout: () -> Unit
    ) = AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        setNegativeButton(android.R.string.ok) { _, _ ->
            logout.invoke()
        }
        setCancelable(false)
        show()
    }

    fun dialogError(message: String) = AlertDialog.Builder(this).apply {
        setTitle(R.string.error)
        setMessage(message)
        setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }
        setCancelable(false)
        show()
    }

}
