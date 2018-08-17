package com.example.hexa_aaronlee.opentriviadb_demo.API

import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.QuestionCountData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface QuestionCountApi {

    @GET
    fun getQuestionCountData(@Url uriTxt: String): Observable<QuestionCountData>
}