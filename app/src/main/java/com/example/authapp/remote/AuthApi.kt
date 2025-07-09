package com.example.authapp.remote
interface AuthApi {
    @POST("register")
    suspend fun register(@Body user: UserCreate): Response<UserOut>

    @POST("login")
    suspend fun login(@Body request: LoginRequest):
            Response<TokenResponse>

    @GET("me")
    suspend fun me(@Header("Authorization" ) token: String):
            Response<UserOut>