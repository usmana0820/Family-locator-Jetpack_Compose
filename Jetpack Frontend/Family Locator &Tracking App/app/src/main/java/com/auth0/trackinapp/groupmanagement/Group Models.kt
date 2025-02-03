package com.auth0.trackinapp.groupmanagement


import CreateGroupRequest
import CreateGroupResponse
import DeleteGroupResponse
import EditGroupRequest
import EditGroupResponse
import GroupDetailsResponse
import JoinGroupRequest
import JoinGroupResponse
import LeaveGroupResponse
import UpdateRoleRequest
import UpdateRoleResponse

import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class GroupViewModel : ViewModel() {

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


    // Get Group Details
    fun getGroupDetails(groupId: Int, token: String, onSuccess: (GroupDetailsResponse) -> Unit, onError: (String) -> Unit) {
        val call = RetrofitService.api.getGroupDetails(groupId, "Bearer $token")
        call.enqueue(object : Callback<GroupDetailsResponse> {
            override fun onResponse(call: Call<GroupDetailsResponse>, response: Response<GroupDetailsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    onError("Failed to retrieve group details: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GroupDetailsResponse>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }

    // Leave Group
    fun leaveGroup(groupId: Int, token: String, onSuccess: (LeaveGroupResponse) -> Unit, onError: (String) -> Unit) {
        val call = RetrofitService.api.leaveGroup(groupId, "Bearer $token")
        call.enqueue(object : Callback<LeaveGroupResponse> {
            override fun onResponse(call: Call<LeaveGroupResponse>, response: Response<LeaveGroupResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    onError("Failed to leave group: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LeaveGroupResponse>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }

    // Update Member Role
    fun updateMemberRole(groupId: Int, memberId: Int, token: String, role: String, onSuccess: (UpdateRoleResponse) -> Unit, onError: (String) -> Unit) {
        val updateRoleRequest = UpdateRoleRequest(role)

        val call = RetrofitService.api.updateMemberRole(groupId, memberId, "Bearer $token", updateRoleRequest)
        call.enqueue(object : Callback<UpdateRoleResponse> {
            override fun onResponse(call: Call<UpdateRoleResponse>, response: Response<UpdateRoleResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    onError("Failed to update member role: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UpdateRoleResponse>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }


}

