package com.chococard.carwash.ui.splashscreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.AppService
import com.chococard.carwash.factory.SplashScreenFactory
import com.chococard.carwash.repositories.BaseRepository
import com.chococard.carwash.ui.auth.AuthActivity
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.readPref
import com.chococard.carwash.viewmodel.SplashScreenViewModel

class SplashScreenActivity : BaseActivity<SplashScreenViewModel, SplashScreenFactory>() {

    private val GRANTED = PackageManager.PERMISSION_GRANTED
    private val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION

    override fun viewModel() = SplashScreenViewModel::class.java

    override fun factory() = SplashScreenFactory(repositoryConnection)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        init()
    }

    private fun init() {
        // check permission location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (
                ContextCompat.checkSelfPermission(baseContext, ACCESS_FINE_LOCATION) != GRANTED ||
                ContextCompat.checkSelfPermission(baseContext, ACCESS_COARSE_LOCATION) != GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION
                    ), CommonsConstant.REQUEST_CODE_PERMISSION
                )
            } else {
                authByCheckToken()
            }
        } else {
            authByCheckToken()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CommonsConstant.REQUEST_CODE_PERMISSION) {
            if (
                grantResults[0] != GRANTED ||
                grantResults[1] != GRANTED
            ) {
                finish()
            } else {
                authByCheckToken()
            }
        }
    }

    private fun authByCheckToken() {
        val token = readPref(CommonsConstant.TOKEN)
        if (token.isEmpty()) {
            Intent(baseContext, AuthActivity::class.java).also {
                startActivity(it)
                finish()
            }
        } else {
            Intent(baseContext, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }

}
