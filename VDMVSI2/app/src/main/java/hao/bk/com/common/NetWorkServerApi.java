package hao.bk.com.common;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by T430 on 4/22/2016.
 */
public interface NetWorkServerApi {
    @FormUrlEncoded
    @POST("mem_api.php?publicKey=5628acfce494c53189505f337bfa6870&action=login")
    public Call<JsonObject> login(@FieldMap Map<String, String> users);

    @FormUrlEncoded
    @POST("mem_api.php?publicKey=5628acfce494c53189505f337bfa6870&action=addnew")
    public Call<JsonObject> register(@FieldMap Map<String, String> users);

    @FormUrlEncoded
    @POST("project_api.php?publicKey=5628acfce494c53189505f337bfa6870&action=addProject")
    public Call<JsonObject> addNewProject(@FieldMap Map<String, String> users);

    @FormUrlEncoded
    @POST("project_api.php?publicKey=5628acfce494c53189505f337bfa6870&action=editProject")
    public Call<JsonObject> editProject(@FieldMap Map<String, String> users);

    @FormUrlEncoded
    @POST("mess_api.php?publicKey=5628acfce494c53189505f337bfa6870&action=getListMessUser")
    public Call<JsonObject> getChatMessageTwoUser(@FieldMap Map<String, String> users);

    @FormUrlEncoded
    @POST("mess_api.php?publicKey=5628acfce494c53189505f337bfa6870&action=setMess")
    public Call<JsonObject> setMess(@FieldMap Map<String, String> users);

    @FormUrlEncoded
    @POST("mem_api.php?publicKey=5628acfce494c53189505f337bfa6870&action=changepass")
    public Call<JsonObject> changePass(@FieldMap Map<String, String> users);

    @GET("api/project_api.php")
    public Call<JsonObject> getNews(@QueryMap Map<String, String> maps);

    @GET("api/project_api.php")
    public Call<JsonObject> runCareProject(@QueryMap Map<String, String> maps);
    @GET("api/project_api.php")
    public Call<JsonObject> getListCare(@QueryMap Map<String, String> maps);

    @GET("api/news_api.php")
    public Call<JsonObject> getNewsVis(@QueryMap Map<String, String> maps);

    @GET("api/mem_api.php")
    public Call<JsonObject> getUserInfo(@QueryMap Map<String, String> maps);

    @GET("api/mem_api.php")
    public Call<JsonObject> getAllUser(@QueryMap Map<String, String> maps);

    @GET("api/mess_api.php?publicKey=5628acfce494c53189505f337bfa6870&action=getListLateMess")
    public Call<JsonObject> getLastMessage(@QueryMap Map<String, String> maps);

}
