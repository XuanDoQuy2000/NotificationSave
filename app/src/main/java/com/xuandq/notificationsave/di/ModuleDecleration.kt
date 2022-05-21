package com.xuandq.notificationsave.di

import androidx.room.Room
import com.google.gson.GsonBuilder
import com.xuandq.litekoin.core.module
import com.xuandq.notificationsave.SplashViewModel
import com.xuandq.notificationsave.data.NotyRepository
import com.xuandq.notificationsave.data.database.AppDatabase
import com.xuandq.notificationsave.data.file.NotyFileStorageImpl
import com.xuandq.notificationsave.data.share_pref.NotySharedPreferenceImpl
import com.xuandq.notificationsave.ui.block_noti.BlockNotiViewModel
import com.xuandq.notificationsave.ui.list_app_noti.ListNotiViewModel
import com.xuandq.notificationsave.ui.list_notification.NotificationViewModel
import com.xuandq.notificationsave.ui.list_title.ListTitleViewModel
import com.xuandq.notificationsave.ui.setting.SettingViewModel

val repositoryModules = module {
    single {
        GsonBuilder().create()
    }

    single {
        NotySharedPreferenceImpl(get(), get())
    }

    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "prox_database"
        ).build()
    }

    single {
        NotyFileStorageImpl(get())
    }

    single {
        NotyRepository(get(),get(),get())
    }
}

val viewModels = module {
    viewModel { ListNotiViewModel() }
    viewModel { BlockNotiViewModel(get()) }
    viewModel { SettingViewModel() }
    viewModel { SplashViewModel(get()) }
    viewModel { ListTitleViewModel() }
    viewModel { NotificationViewModel() }
}

val appModules = repositoryModules + viewModels