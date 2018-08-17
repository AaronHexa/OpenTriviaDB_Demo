package com.example.hexa_aaronlee.opentriviadb_demo.API

import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.QuestionData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface QuestionApi {

    @GET
    fun getSelectedQuestion(@Url token: String): Observable<QuestionData>
}