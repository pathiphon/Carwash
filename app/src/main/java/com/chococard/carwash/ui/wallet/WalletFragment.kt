package com.chococard.carwash.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.AppService
import com.chococard.carwash.factory.WalletFactory
import com.chococard.carwash.repositories.BaseRepository
import com.chococard.carwash.ui.addwallet.AddWalletActivity
import com.chococard.carwash.ui.base.BaseFragment
import com.chococard.carwash.util.extension.dialogDatePicker
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.show
import com.chococard.carwash.util.extension.toast
import com.chococard.carwash.viewmodel.WalletViewModel
import kotlinx.android.synthetic.main.fragment_wallet.*

class WalletFragment : BaseFragment<WalletViewModel, WalletFactory>(R.layout.fragment_wallet) {

    override fun viewModel() = WalletViewModel::class.java

    override fun factory() = WalletFactory(BaseRepository(AppService.invoke(requireContext())))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        // set event
        iv_calendar.setOnClickListener {
            activity?.dialogDatePicker { begin ->
                val (bDayOfMonth, bMonth, bYear) = begin
                val dateBegin = "$bDayOfMonth/$bMonth/$bYear"

                activity?.dialogDatePicker { end ->
                    val (eDayOfMonth, eMonth, eYear) = end
                    val dateEnd = "$eDayOfMonth/$eMonth/$eYear"

                    progress_bar.show()
                    viewModel.callFetchWallet(dateBegin, dateEnd)
                }
            }
        }

        fab.setOnClickListener {
            Intent(context, AddWalletActivity::class.java).apply {
                startActivity(this)
            }
        }

        // observe
        viewModel.getWallet.observe(viewLifecycleOwner, Observer {
            progress_bar.hide()
            context.toast(it.toString())
        })

        viewModel.getError.observe(viewLifecycleOwner, Observer {
            progress_bar.hide()
            context.toast(it, Toast.LENGTH_LONG)
        })
    }

    override fun onResume() {
        super.onResume()

        // call api
        progress_bar.show()
        viewModel.callFetchWallet()
    }

}