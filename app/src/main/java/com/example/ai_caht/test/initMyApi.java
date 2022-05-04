package com.example.ai_caht.test;

import com.example.ai_caht.test.Chat.ChatRequest;
import com.example.ai_caht.test.Chat.ChatResponse;
import com.example.ai_caht.test.Login.LoginRequest;
import com.example.ai_caht.test.Login.LoginResponse;
import com.example.ai_caht.test.Signup.SignupRequest;
import com.example.ai_caht.test.Signup.SignupResponse;
import com.example.ai_caht.test.state.ParrotState;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
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

    //
    @POST("app/chat")
    //@헤더를 보낼 경우에
    //Call<ChatResponse> getChatResponse(@Header("Authorization") String authorization);
    Call<ChatResponse> getChatResponse(@Body ChatRequest chatRequest);

    @HTTP(method = "DELETE", path = "app/chat", hasBody = true)
    Call<ChatResponse> deleteChat(@Body ChatRequest chatRequest);
}

