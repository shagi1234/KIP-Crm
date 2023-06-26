package tm.payhas.crm.api.service;

import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tm.payhas.crm.api.request.RequestComment;
import tm.payhas.crm.api.request.RequestCreateTask;
import tm.payhas.crm.api.request.RequestNewChecklist;
import tm.payhas.crm.api.request.RequestNewProject;
import tm.payhas.crm.api.request.RequestNews;
import tm.payhas.crm.api.request.RequestTaskComment;
import tm.payhas.crm.api.request.RequestUserTasks;
import tm.payhas.crm.api.response.ResponseAddNews;
import tm.payhas.crm.api.response.ResponseChecklist;
import tm.payhas.crm.api.response.ResponseComment;
import tm.payhas.crm.api.response.ResponseDashboard;
import tm.payhas.crm.api.response.ResponseDashboardItem;
import tm.payhas.crm.api.response.ResponseDataFolder;
import tm.payhas.crm.api.response.ResponseDeleteFile;
import tm.payhas.crm.api.response.ResponseGroupInfo;
import tm.payhas.crm.api.response.ResponseManyFiles;
import tm.payhas.crm.api.response.ResponseOneMessage;
import tm.payhas.crm.api.response.ResponseOneProject;
import tm.payhas.crm.api.response.ResponseOneTask;
import tm.payhas.crm.api.response.ResponseProjects;
import tm.payhas.crm.api.response.ResponseRoomMessages;
import tm.payhas.crm.api.response.ResponseSignIn;
import tm.payhas.crm.api.response.ResponseSingleFile;
import tm.payhas.crm.api.response.ResponseTaskComment;
import tm.payhas.crm.api.response.ResponseTasks;
import tm.payhas.crm.api.response.ResponseUserGroup;
import tm.payhas.crm.api.response.ResponseUserInfo;
import tm.payhas.crm.api.response.ResponseUsersList;

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


    @GET("users/all")
    Call<ResponseUsersList> getAllUsers();


    @GET("projects/participants/{id}")
    Call<ResponseUsersList> getProjectsUserList(@Path("id") int id);


    @GET("projects/{id}")
    Call<ResponseOneProject> getOneProject(@Header("Authorization") String token, @Path("id") int id, @Query("page") int page, @Query("limit") int limit);


    @Headers({"Content-Type: application/json"})
    @POST("projects")
    Call<ResponseOneProject> addNewProject(@Header("Authorization") String token, @Body RequestNewProject requestNewProject);

    @GET("chat/room/messages/mobile/{id}")
    Call<ResponseRoomMessages> getMessageRoom(@Header("Authorization") String token, @Path("id") int id, @Query("page") int page, @Query("limit") int limit);

    @Headers({"Accept: multipart/form-data"})
    @Multipart
    @POST("single/upload/messages")
    Call<ResponseSingleFile> uploadFile(@Part MultipartBody.Part fileUrl);

    @Headers({"Accept: multipart/form-data"})
    @Multipart
    @POST("filestorage/mobile/file")
    Call<ResponseSingleFile> uploadFileToCLoud(@Part("originalFileName") RequestBody originalName, @Part("directoryUrl") RequestBody directoryUrl, @Part MultipartBody.Part fileUrl);

    @Headers({"Accept: multipart/form-data"})
    @Multipart
    @POST("upload/news")
    Call<ResponseManyFiles> uploadFiles(@Part List<MultipartBody.Part> fileUrl);

    @POST("/chat/message/remove/{id}")
    Call<ResponseOneMessage> removeMessage(@Header("Authorization") String token, @Path("id") int id);

    @GET("chat/participants/user/{id}")
    Call<ResponseUserInfo> getUserInfo(@Header("Authorization") String token, @Path("id") int id);


    @GET("chat/participants/{id}")
    Call<ResponseGroupInfo> getGroupInfo(@Header("Authorization") String token, @Path("id") int id);

    @GET("home/dashboard")
    Call<ResponseDashboard> getDashboard(@Query("searchStr") String search, @Query("page") int page, @Query("limit") int limit);

    @GET("home/news/{id}")
    Call<ResponseDashboardItem> getDashboardItem(@Header("Authorization") String token, @Path("id") int id, @Query("page") int page, @Query("limit") int limit);

    @Headers({"Content-Type: application/json"})
    @POST("home/comment")
    Call<ResponseComment> addComment(@Header("Authorization") String token, @Body RequestComment requestComment);

    @Headers({"Content-Type: application/json"})
    @POST("home/news")
    Call<ResponseAddNews> addNewsToDashboard(@Header("Authorization") String token, @Body RequestNews requestNews);


    @GET("filestorage/root")
    Call<ResponseDataFolder> getCloudFolder();


    @GET("filestorage/folder/{id}")
    Call<ResponseDataFolder> getFolderFiles(@Path("id") String id);


    @Headers({"Content-Type: application/json"})
    @POST("filestorage/remove")
    Call<ResponseDeleteFile> removeFile(@Body JsonObject jsonObject);


    @GET("tasks/{id}")
    Call<ResponseOneTask> getOneTask(@Header("Authorization") String token, @Path("id") int id);

    @Headers({"Content-Type: application/json"})
    @POST("tasks/checklist")
    Call<ResponseChecklist> createNewChecklist(@Header("Authorization") String token, @Body RequestNewChecklist requestNewChecklist);


    @Headers({"Content-Type: application/json"})
    @POST("tasks/checklist/complete/{id}")
    Call<ResponseChecklist> completeChecklist(@Header("Authorization") String token, @Path("id") int id);


    @Headers({"Content-Type: application/json"})
    @POST("tasks/comment")
    Call<ResponseTaskComment> createComment(@Header("Authorization") String token, @Body RequestTaskComment taskComment);


    @Headers({"Content-Type: application/json"})
    @PUT("tasks/status/{id}")
    Call<ResponseOneTask> changeTaskStatus(@Header("Authorization") String token, @Path("id") int id);


}
