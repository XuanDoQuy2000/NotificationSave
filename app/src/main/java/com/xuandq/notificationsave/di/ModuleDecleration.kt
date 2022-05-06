package com.xuandq.notificationsave.di

import android.content.Context
import android.os.Build
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.xuandq.litekoin.core.module
import com.xuandq.notificationsave.data.NotyRepository
import com.xuandq.notificationsave.data.database.AppDatabase
import com.xuandq.notificationsave.data.file.NotyFileStorageImpl
import com.xuandq.notificationsave.data.share_pref.NotySharedPreferenceImpl
import com.xuandq.notificationsave.ui.block_noti.BlockNotiViewModel
import com.xuandq.notificationsave.ui.list_noti.ListNotiViewModel
import com.xuandq.notificationsave.ui.setting.SettingViewModel
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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
    viewModel { ListNotiViewModel(get()) }
    viewModel { BlockNotiViewModel() }
    viewModel { SettingViewModel() }
}

val appModules = repositoryModules + viewModels