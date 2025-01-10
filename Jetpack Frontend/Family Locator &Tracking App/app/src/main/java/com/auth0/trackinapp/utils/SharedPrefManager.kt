package com.auth0.trackinapp.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    companion object {
        private const val USER_ID_KEY = "user_id"
        private const val GROUP_NAME_KEY = "group_name"
    }

    // Save User ID
    fun saveUserId(userId: String) {
        sharedPreferences.edit().putString(USER_ID_KEY, userId).apply()
    }

    // Get User ID
    fun getUserId(): String? {
        return sharedPreferences.getString(USER_ID_KEY, null)
    }

    // Save Group Name
    fun saveGroupName(groupName: String) {
        sharedPreferences.edit().putString(GROUP_NAME_KEY, groupName).apply()
    }

    // Get Group Name
    fun getGroupName(): String? {
        return sharedPreferences.getString(GROUP_NAME_KEY, null)
    }

    // Clear All Data
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}
