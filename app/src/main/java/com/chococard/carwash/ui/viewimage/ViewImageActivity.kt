package com.chococard.carwash.ui.viewimage

import android.os.Bundle
import android.view.MenuItem
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.changepassword.ChangePasswordActivity
import com.chococard.carwash.ui.changeprofile.ChangeProfileActivity
import com.chococard.carwash.ui.splashscreen.SplashScreenActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ViewImageViewModel
import kotlinx.android.synthetic.main.activity_view_image.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ViewImageActivity : BaseActivity() {

    val viewModel by viewModel<ViewImageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)

        // set toolbar
        setToolbar(toolbar)

        val image = intent.getStringExtra(CommonsConstant.IMAGE)
        if (image != null) {
            iv_image_place_holder.hide()
            iv_image.setImageFromInternet(image)
        }

        // observe
        viewModel.state.observe { state ->
            if (state.loading) progress_bar.show() else progress_bar.hide()
        }

        viewModel.getLogout.observe { response ->
            val (success, message) = response
            if (success) {
                startActivity<SplashScreenActivity> {
                    finishAffinity()
                }
            } else {
                root_layout.snackbar(message)
            }
        }

        viewModel.error.observeError()

        // event
        iv_arrow_back.setOnClickListener { onBackPressed() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_change_profile -> startActivity<ChangeProfileActivity>()
            R.id.option_change_password -> startActivity<ChangePasswordActivity>()
            R.id.option_contact_admin -> dialogContactAdmin { startActivityActionDial() }
            R.id.option_logout -> dialogLogout { viewModel.callLogout() }
        }
        return super.onOptionsItemSelected(item)
    }

}
