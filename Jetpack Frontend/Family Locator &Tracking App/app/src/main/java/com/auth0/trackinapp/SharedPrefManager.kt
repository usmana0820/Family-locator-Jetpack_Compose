package com.auth0.trackinapp
import android.content.Context
import android.content.SharedPreferences

object SharedPrefManager {

    private const val PREF_NAME = "user_pref"      // SharedPreferences file name for storing user data
    private const val USER_TOKEN = "USER_TOKEN"     // Key used to store the user token


    private lateinit var sharedPreferences: SharedPreferences     // SharedPreferences instance


    // Initialize SharedPreferences with the application context
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Save the user's token to SharedPreferences
    fun saveToken(token: String) {
        // Storing the token securely using SharedPreferences
        sharedPreferences.edit().putString(USER_TOKEN, token).apply()
    }

    // Retrieve the token from SharedPreferences
    fun getToken(): String? {
        // Fetching the token from SharedPreferences, or null if it doesn't exist
        return sharedPreferences.getString(USER_TOKEN, null)
    }

    // Check if a token is available (i.e., if the token exists in SharedPreferences)
    fun isTokenAvailable(): Boolean {
        return getToken() != null // Returns true if the token is found, else false
    }

    // Clear the stored token (i.e., log out the user)
    fun clearToken() {
        // Removing the token from SharedPreferences
        sharedPreferences.edit().remove(USER_TOKEN).apply()
    }
}
