package com.example.hexa_aaronlee.opentriviadb_demo.API

import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.QuestionCountData
import io.reactivex.Observable
import retrofit2.http.GET

interface QuestionCountApi {

    @GET("api_count_global.php")
    fun getQuestionCountData() : Observable<QuestionCountData>
}