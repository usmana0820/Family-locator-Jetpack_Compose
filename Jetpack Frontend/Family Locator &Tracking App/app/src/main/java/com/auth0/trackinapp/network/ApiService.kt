// ApiService.kt
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


// request and response models for each of these operations. These models will represent the data that is sent to and received from the API.

                                                    //Authentication
data class RegisterRequest( val email: String, val password: String, val confirmPassword: String,  val phoneNumber: String)
data class ForgotPasswordRequest(val email: String)
data class ResetPasswordRequest(val token: String, val newPassword: String, val confirmPassword: String)



data class LoginRequest(val email: String, val password: String)

data class LoginResponse(val refresh: String, val access: String)

                                                    //GroupManagement
// GroupRequest.kt
data class CreateGroupRequest(val group_name: String, val max_members: Int)
data class CreateGroupResponse(val message: String, val group_id: Int, val group_name: String, val pin: Int)

data class JoinGroupRequest(val pin: Int)
data class JoinGroupResponse(val message: String, val group_id: Int, val group_name: String)


data class EditGroupRequest(val group_name: String?, val max_members: Int?)
data class EditGroupResponse(val message: String, val group_name: String, val max_members: Int)

data class DeleteGroupResponse(val message: String)
//data class LeaveGroupResponse(val message: String)


// GroupDetailsResponse.kt
data class GroupDetailsResponse(
  val id: Int,
  val group_name: String,
  val max_members: Int,
  val current_member_count: Int,
  val members: List<GroupMember>
)

data class GroupMember(
  val id: Int,
  val user: String,
  val user_name: String,
  val user_email: String,
  val role: String,
  val joined_at: String
)


data class GroupDetails(
  val id: Int,
  val group_name: String,
  val max_members: Int,
  val current_member_count: Int,
  val members: List<Member>
)

data class Member(
  val id: Int,
  val user: String,
  val user_name: String,
  val user_email: String,
  val role: String,
  val joined_at: String
)

// Leave Group Response Model
data class LeaveGroupResponse(val status: String, val message: String)

// Update Role Request Model
data class UpdateRoleRequest(val role: String)

// Update Role Response Model
data class UpdateRoleResponse(val status: String, val message: String)

interface ApiService {
                                                      //EndPoints
  //Authentication

  @POST("register/")
  fun signUp(@Body request: RegisterRequest): Call<Void>

  @GET("verify-email/{token}/")
  fun verifyEmail(@Path("token") token: String): Call<Void>

  @POST("login/")
  fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

  @POST("forgot-password/")
  fun forgotPassword(@Body request: ForgotPasswordRequest): Call<Void>

  @POST("reset-password/{token}/")
  fun resetPassword(@Path("token") token: String, @Body request: ResetPasswordRequest): Call<Void>



                                                  //GroupManagement
  // Create Group
  @POST("groups/create/")
  fun createGroup(@Header("Authorization") token: String, @Body groupRequest: CreateGroupRequest): Call<CreateGroupResponse>

  // Join Group
  @POST("groups/join/")
  fun joinGroup(@Header("Authorization") token: String, @Body joinRequest: JoinGroupRequest): Call<JoinGroupResponse>


  // Get Group Details
  @GET("api/group_details/{group_id}/")
  fun getGroupDetails(
    @Path("group_id") groupId: Int,
    @Header("Authorization") token: String
  ): Call<GroupDetailsResponse>

  // Leave Group
  @POST("groups/leave/{group_id}/")
  fun leaveGroup(
    @Path("group_id") groupId: Int,
    @Header("Authorization") token: String
  ): Call<LeaveGroupResponse>

  // Update Member Role
  @PATCH("groups/update_member_role/{group_id}/{member_id}/")
  fun updateMemberRole(
    @Path("group_id") groupId: Int,
    @Path("member_id") memberId: Int,
    @Header("Authorization") token: String,
    @Body updateRoleRequest: UpdateRoleRequest
  ): Call<UpdateRoleResponse>




}



