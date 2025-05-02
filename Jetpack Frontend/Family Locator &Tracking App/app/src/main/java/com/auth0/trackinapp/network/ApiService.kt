// ApiService.kt
import com.google.android.gms.common.api.Response
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


data class UserInfoResponse(val id: Int,
  val email: String,
  val phoneNumber: String,
  val isVerified: Boolean
)


                                                    //GroupManagement
// GroupRequest.kt
data class CreateGroupRequest(val group_name: String, val max_members: Int)
data class CreateGroupResponse(val message: String, val group_id: Int, val group_name: String, val pin: Int)

data class JoinGroupRequest(val pin: Int)
data class JoinGroupResponse(val message: String, val group_id: Int, val group_name: String)

data class Groupdetails(val id: Int, val group_name: String, val max_members: Int, val pin: Int, val created_by: String)

data class UpdateRoleRequest(val role: String)
data class GenericResponse(val message: String)





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

  @GET("user_groupsdetail/")
  suspend fun getUserGroupsDetails(@Header("Authorization") token: String): List<Groupdetails>  // Assuming the response is a list of groups


  // Leave Group
  @POST("groups/leave/{group_id}/")
  fun leaveGroup(@Header("Authorization") token: String, @Path("group_id") groupId: Int): Call<GenericResponse>

  // Update Member Role
  @PATCH("groups/update-role/{group_id}/{member_id}/")
  fun updateMemberRole(@Header("Authorization") token: String, @Path("group_id") groupId: Int, @Path("member_id") memberId: Int,
                       @Body roleUpdate: UpdateRoleRequest
  ): Call<GenericResponse>

  // Delete Group
  @DELETE("groups/delete/{group_id}/")
  fun deleteGroup(@Header("Authorization") token: String, @Path("group_id") groupId: Int
  ): Call<GenericResponse>




}



