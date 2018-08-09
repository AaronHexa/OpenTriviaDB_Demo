package com.example.hexa_aaronlee.opentriviadb_demo.API

import com.example.hexa_aaronlee.opentriviadb_demo.Object_and_Data.TokenObject
import io.reactivex.Observable
import retrofit2.http.GET

interface TokenAPI {

    @GET("/api_token.php?command=request")
    fun getToken() : Observable<TokenObject>
}