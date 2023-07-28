package com.example.cqlsystechnologies.utils

import android.content.Context

class SharedPreferenceManager(private val context: Context) {

    private fun getSharedPreference() =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    private fun getEditor() = getSharedPreference().edit()


    fun saveString(key: String, value: String) = getEditor().putString(key, value).apply()

    fun saveBoolean(key: String, value: Boolean) = getEditor().putBoolean(key, value).apply()


    fun getString(key: String, default: String? = null): String? =
        getSharedPreference().getString(key, default)

    fun getBoolean(key: String, default: Boolean): Boolean =
        getSharedPreference().getBoolean(key, default)

    fun saveNumber(key: String, value: Long) = getEditor().putLong(key, value).apply()

    fun saveIntegerNumber(key: String, value: Int) = getEditor().putInt(key, value).apply()

    fun getNumber(key: String, default: Long = 0): Long =
        getSharedPreference().getLong(key, default)

    fun getIntegerNumber(key: String, default: Int = 0): Int =
        getSharedPreference().getInt(key, default)

    fun removeValue(key: String) = getEditor().remove(key).commit()

    companion object {
        const val SHARED_PREF_NAME = "com.cqlsystechnologies.SharedPreferenceManager"
    }

}