package com.xuandq.notificationsave.ui

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xuandq.core.BaseActivity
import com.xuandq.core.showAlert
import com.xuandq.notificationsave.R
import com.xuandq.notificationsave.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_home

    override fun initData() {

    }

    override fun initView() {
        setLightStatusBar(true)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.nav_bottom)
            .setupWithNavController(navController)

        if (!isNotificationServiceEnabled(this)) {
            showAlert(
                titleResId = R.string.noty_label_need_permission,
                messageResId = R.string.noty_label_need_permission_description,
                singleButton = false,
                positiveAction = {
                    startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
                }
            )
        }
    }

    override fun initEvent() {

    }

    override fun onObserve() {

    }

    fun isNotificationServiceEnabled(context: Context): Boolean =
        NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.packageName)

    companion object {
        const val ACTION_NOTIFICATION_LISTENER_SETTINGS =
            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
    }
}