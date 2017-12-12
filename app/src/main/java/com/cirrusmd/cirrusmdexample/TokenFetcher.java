package com.cirrusmd.cirrusmdexample;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Cory Clark on 12/8/17
 */

public interface TokenFetcher {
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("sandbox/sessions")
    Call<Token> getSessionJwt(@Body() TokenRequest request);


    @DELETE("sandbox/history")
    Call<Void> deleteUserHistory(@Body() TokenRequest request);
}
