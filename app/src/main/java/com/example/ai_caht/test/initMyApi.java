package com.example.ai_caht.test;

import com.example.ai_caht.test.Chat.ChatRequest;
import com.example.ai_caht.test.Chat.ChatResponse;
import com.example.ai_caht.test.Chat.ImageRequest;
import com.example.ai_caht.test.Chat.ImageResponse;
import com.example.ai_caht.test.Chat.ParrotTalkRequest;
import com.example.ai_caht.test.Chat.ParrotTalkResponse;
import com.example.ai_caht.test.Login.LoginRequest;
import com.example.ai_caht.test.Login.LoginResponse;
import com.example.ai_caht.test.Record.PageSize;
import com.example.ai_caht.test.Record.ParrotRecord;
import com.example.ai_caht.test.Signup.SignupRequest;
import com.example.ai_caht.test.Signup.SignupResponse;
import com.example.ai_caht.test.state.ParrotState;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface initMyApi {
    //@통신 방식("통신 API명")
    @POST("/app/signup")
    Call<String> getSignupResponse(@Body SignupRequest signupRequest);

    @POST("/login")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);

    //
    @GET("app/duplicate/{id}")
    Call<IDduplicateResponse> getidduplicateResponse(@Path("id") String idduplicate);

    @GET("app/state/{id}")
    Call<ParrotState> getParrotState(@Path("id") String UserId);

    @POST("app/state/{id}")
    Call<ParrotState> sendParrotState(@Path("id") String UserId, @Body ParrotState parrotState);

    @GET("app/parrotdata/{id}/{page}")
    Call<ParrotRecord> getParrotRecord(@Path("id") String UserId, @Path("page") String page);

    @POST("app/parrotdata/{id}/{page}")
    Call<ParrotRecord> sendParrotRecord(@Path("id") String UserId, @Path("page") String page, @Body ParrotRecord parrotRecord);

    @GET("app/parrotdata/pagesize/{id}")
    Call<PageSize> getPageSize(@Path("id") String UserId);


    //
    @POST("app/chat")
    //@헤더를 보낼 경우에
    //Call<ChatResponse> getChatResponse(@Header("Authorization") String authorization);
    Call<ChatResponse> getChatResponse(@Body ChatRequest chatRequest);

    @HTTP(method = "DELETE", path = "app/chat", hasBody = true)
    Call<ChatResponse> deleteChat(@Body ChatRequest chatRequest);

    @POST("app/parrottalk")
    Call<ParrotTalkResponse> parrotTalk(@Body ParrotTalkRequest parrotTalkRequest);

    @POST("file/sendimage")
    Call<ImageResponse> imageSend(@Body ImageRequest imageRequest);
}

