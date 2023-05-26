package tm.payhas.crm.api.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tm.payhas.crm.api.response.ResponseRoomMessages;
import tm.payhas.crm.api.response.ResponseSignIn;
import tm.payhas.crm.api.request.RequestCreateTask;
import tm.payhas.crm.api.request.RequestUserTasks;
import tm.payhas.crm.api.response.ResponseProjects;
import tm.payhas.crm.api.response.ResponseTasks;
import tm.payhas.crm.api.response.ResponseUserGroup;

public interface Services {

    @Headers({"Content-Type: application/json"})
    @POST("/auth/singin")
    Call<ResponseSignIn> signIn(@Body JsonObject jsonObject);

    @Headers({"Content-Type: application/json"})
    @PATCH("tasks/user")
    Call<ResponseTasks> getUserTasks(@Header("Authorization") String token, @Body RequestUserTasks requestUserTasks);

    @Headers({"Content-Type: application/json"})
    @POST("tasks")
    Call<ResponseTasks> createTask(@Header("Authorization") String token, @Body RequestCreateTask requestCreateTask);

    @GET("chat/rooms")
    Call<ResponseUserGroup> getContacts(@Header("Authorization") String token);

    @GET("projects/all")
    Call<ResponseProjects> getAllProjects();

    @GET("chat/room/messages/mobile/{id}")
    Call<ResponseRoomMessages> getMessageRoom(@Header("Authorization") String token, @Path("id") int id, @Query("page") int page, @Query("limit") int limit);


}
