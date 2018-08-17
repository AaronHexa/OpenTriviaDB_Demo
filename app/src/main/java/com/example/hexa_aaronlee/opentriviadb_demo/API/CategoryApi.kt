package com.example.hexa_aaronlee.opentriviadb_demo.API

import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.CategoryData
import io.reactivex.Observable
import retrofit2.http.GET

interface CategoryApi {

    @GET("api_category.php")
    fun getAllCategoryData(): Observable<CategoryData>
}

