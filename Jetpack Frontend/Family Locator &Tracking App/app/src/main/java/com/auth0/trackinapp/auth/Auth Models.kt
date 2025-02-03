package com.auth0.trackinapp.auth

import LoginRequest
import LoginResponse
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    fun login(email: String, password: String, onSuccess: (LoginResponse) -> Unit, onError: (String) -> Unit) {
        val loginRequest = LoginRequest(email, password)
        RetrofitService.api.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    onError("Login failed")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }
}
