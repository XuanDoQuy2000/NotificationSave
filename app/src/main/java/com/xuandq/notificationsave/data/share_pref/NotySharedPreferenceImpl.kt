package com.xuandq.notificationsave.data.share_pref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.xuandq.core.isTrue
import com.xuandq.notificationsave.shared.Constants

class NotySharedPreferenceImpl(private val context: Context, val gson: Gson) :
    NotySharedPreference {
    private val pref: SharedPreferences by lazy {
        context.getSharedPreferences(Constants.NOTY_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private operator fun <T> get(key: String, anonymousClass: Class<T>): T? {
        return when (anonymousClass) {
            String::class.java -> pref.getString(key, null) as? T?
            Boolean::class.java -> java.lang.Boolean.valueOf(pref.getBoolean(key, false)) as T?
            Float::class.java -> java.lang.Float.valueOf(pref.getFloat(key, -1f)) as T?
            Int::class.java -> Integer.valueOf(pref.getInt(key, -1)) as T?
            Long::class.java -> java.lang.Long.valueOf(pref.getLong(key, -1)) as T?
            else -> {
                try {
                    gson.fromJson(pref.getString(key, null), anonymousClass)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
    }

    private fun <T> put(key: String, data: T) {
        val editor = pref.edit()
        when (data) {
            is String -> editor.putString(key, data as String)
            is Boolean -> editor.putBoolean(key, data as Boolean)
            is Float -> editor.putFloat(key, data as Float)
            is Int -> editor.putInt(key, data as Int)
            is Long -> editor.putLong(key, data as Long)
            else -> editor.putString(key, gson.toJson(data))
        }
        editor.apply()
    }

    private fun remove(key: String) {
        val editor = pref.edit()
        editor.remove(key)
        editor.apply()
    }

    fun isFirstLaunch() = get(KEY_FIRST_LAUNCH, Boolean::class.java).isTrue()

    fun setFirstLaunch(value : Boolean) {
        put(KEY_FIRST_LAUNCH, value)
    }

    companion object {
        private const val KEY_TELCO_LISTING = "com.xuandq.noty.pref.telcos"
        private const val KEY_CURRENT_PHONE = "com.xuandq.noty.pref.current_phone"
        private const val KEY_CURRENT_CUSTOMER_INFORMATION =
            "com.xuandq.noty.pref.current_customer_information"
        private const val KEY_RECENT_PROFILE = "com.xuandq.noty.pref.recent_profile"
        private const val KEY_FIRST_LAUNCH = "com.xuandq.noty.pref.frist_launch"
    }

}