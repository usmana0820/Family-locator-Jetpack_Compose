package com.auth0.trackinapp.groupmanagement

import CreateGroupRequest
import CreateGroupResponse
import GenericResponse
import Groupdetails
import JoinGroupRequest
import JoinGroupResponse
import UpdateRoleRequest
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class GroupViewModel() : ViewModel() {

    private val _groupDetails = MutableLiveData<List<Groupdetails>>()
    val groupDetails: LiveData<List<Groupdetails>> = _groupDetails

    fun createGroup(token: String, groupName: String, maxMembers: Int, onSuccess: (CreateGroupResponse) -> Unit, onError: (String) -> Unit) {
        val groupRequest = CreateGroupRequest(groupName, maxMembers)

        // Adding Authorization header with Bearer token
        val call = RetrofitService.api.createGroup("Bearer $token", groupRequest)

        call.enqueue(object : Callback<CreateGroupResponse> {
            override fun onResponse(call: Call<CreateGroupResponse>, response: Response<CreateGroupResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    onError("Group creation failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CreateGroupResponse>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }





    fun joinGroup(token: String, pin: Int, onSuccess: (JoinGroupResponse) -> Unit, onError: (String) -> Unit) {
        val joinRequest = JoinGroupRequest(pin)

        val call = RetrofitService.api.joinGroup("Bearer $token", joinRequest)
        call.enqueue(object : Callback<JoinGroupResponse> {
            override fun onResponse(call: Call<JoinGroupResponse>, response: Response<JoinGroupResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    onError("Joining group failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<JoinGroupResponse>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }



    // Fetch groups with token passed dynamically
    fun fetchGroupDetails(token: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitService.api.getUserGroupsDetails("Bearer $token")
                if (response.isNotEmpty()) {
                    _groupDetails.postValue(response)
                } else {
                    Log.e("GroupViewModel", "No groups found")
                }
            } catch (e: Exception) {
                Log.e("GroupViewModel", "Error fetching groups: ${e.localizedMessage}")
            }
        }
    }


    fun leaveGroup(token: String, groupId: Int, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val call = RetrofitService.api.leaveGroup("Bearer $token", groupId)
        call.enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it.message) }
                } else {
                    onError("Failed to leave group: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }

    fun updateMemberRole(token: String, groupId: Int, memberId: Int, newRole: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val roleUpdate = UpdateRoleRequest(newRole)
        val call = RetrofitService.api.updateMemberRole("Bearer $token", groupId, memberId, roleUpdate)
        call.enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it.message) }
                } else {
                    onError("Role update failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }

    fun deleteGroup(token: String, groupId: Int, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val call = RetrofitService.api.deleteGroup("Bearer $token", groupId)
        call.enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it.message) }
                } else {
                    onError("Delete failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }


}

