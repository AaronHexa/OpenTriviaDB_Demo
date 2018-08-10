package com.example.hexa_aaronlee.opentriviadb_demo.API

import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.ResetData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface ResetApi {

    @GET
    fun resetToken(@Url token : String) : Observable<ResetData>

}