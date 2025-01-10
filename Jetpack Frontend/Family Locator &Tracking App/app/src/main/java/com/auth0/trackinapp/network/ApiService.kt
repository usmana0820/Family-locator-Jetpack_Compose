package com.auth0.trackinapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

// Define the API interface with necessary endpoints
interface ApiService {

    // Verify the token endpoint (you can modify it if required)
    @POST("google_token/")
    suspend fun verifyToken(@Body request: TokenRequest): Response<UserResponse>

    // Check if the group name already exists
    @GET("check-group-name/")
    suspend fun checkGroupName(@Query("group_name") groupName: String): Response<GroupNameResponse>

    // Create a new group
    @POST("create-group/")
    suspend fun createGroup(@Body request: CreateGroupRequest): Response<CreateGroupResponse>

    // Verify the pin for joining a group
    @POST("verify-pin/")
    suspend fun verifyPin(@Body request: VerifyPinRequest): Response<VerifyPinResponse>

    companion object {
        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl("http://192.168.10.4:8000/api/") // Make sure to update the base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}

// Request and response models for each API call

// Token verification
data class TokenRequest(val token: String)
data class UserResponse(val name: String, val email: String, val message: String? = null)

// Group name check
data class GroupNameResponse(val exists: Boolean, val message: String? = null)

// Group creation
data class CreateGroupRequest(val groupName: String, val pinCode: String)
data class CreateGroupResponse(val success: Boolean, val message: String, val pin: Int? = null)

// Verify pin for joining
data class VerifyPinRequest(val groupName: String, val pinCode: String)
data class VerifyPinResponse(val success: Boolean, val message: String, val userId: String? = null)
