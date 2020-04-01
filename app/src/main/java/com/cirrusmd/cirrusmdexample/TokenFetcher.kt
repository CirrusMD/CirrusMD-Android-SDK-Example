package com.cirrusmd.cirrusmdexample

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Headers
import retrofit2.http.POST

interface TokenFetcher {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("sandbox/sessions")
    fun getSessionJwt(@Body request: TokenRequest): Call<Token>?

    @DELETE("sandbox/history")
    fun deleteUserHistory(@Body request: TokenRequest): Call<Void>?
}
