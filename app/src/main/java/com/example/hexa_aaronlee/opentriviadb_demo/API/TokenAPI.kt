package com.example.hexa_aaronlee.opentriviadb_demo.API

import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.TokenData
import io.reactivex.Observable
import retrofit2.http.GET

interface TokenAPI {

    @GET("api_token.php?command=request")
    fun getToken(): Observable<TokenData>
}