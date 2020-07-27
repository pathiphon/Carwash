package com.chococard.carwash.ui.payment

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.PaymentViewModel
import kotlinx.android.synthetic.main.activity_payment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentActivity : BaseActivity() {

    val viewModel by viewModel<PaymentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        setToolbar(toolbar)

        // set widgets
        viewModel.getDbJob.observe { job ->
            val (_, _, fullName, imageProfile, _, packageName, price, _, _, _, _, _, dateTime) = job
            tv_date_time.text = dateTime
            tv_full_name.text = fullName
            tv_service.text = packageName
            tv_price.text = price
            iv_photo.setImageCircle(imageProfile)
        }

        iv_arrow_back.setOnClickListener { onBackPressed() }
        bt_payment.setOnClickListener { paymentService() }

        //observe
        viewModel.getPaymentJob.observe { response ->
            progress_bar.hide()
            val (success, message) = response
            if (success) {
                startActivity<MainActivity> {
                    finishAffinity()
                }
            } else {
                root_layout.snackbar(message)
            }
        }

        viewModel.getError.observe {
            progress_bar.hide()
            dialogError(it)
        }
    }

    private fun paymentService() {
        progress_bar.show()
        viewModel.callPaymentJob()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option_busy, menu)
        menu?.findItem(R.id.option_report)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_contact_admin -> dialogContactAdmin { startActivityActionDial() }
        }
        return super.onOptionsItemSelected(item)
    }

}
