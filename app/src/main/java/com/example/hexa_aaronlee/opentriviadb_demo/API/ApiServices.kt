package com.example.hexa_aaronlee.opentriviadb_demo.API

import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiServices {

    @GET("api_category.php")
    fun getAllCategoryData(): Observable<CategoryData>

    @GET
    fun getSelectedQuestion(@Url token: String): Observable<QuestionData>

    @GET
    fun getQuestionCountData(@Url uriTxt: String): Observable<QuestionCountData>

    @GET
    fun resetToken(@Url token: String): Observable<ResetData>

    @GET("api_token.php?command=request")
    fun getToken(): Observable<TokenData>
}