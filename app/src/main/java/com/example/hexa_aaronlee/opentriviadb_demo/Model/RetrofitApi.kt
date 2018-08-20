package com.example.hexa_aaronlee.opentriviadb_demo.Model

import com.example.hexa_aaronlee.opentriviadb_demo.API.TokenAPI
import com.example.hexa_aaronlee.opentriviadb_demo.BuildConfig
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.introspect.VisibilityChecker
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory


class RetrofitApi {
    lateinit var mapper: ObjectMapper
    lateinit var okhttpClientBuilder: OkHttpClient.Builder
    lateinit var logging: HttpLoggingInterceptor

    fun RequestRetrofitApi(): Retrofit {
        okhttpClientBuilder = OkHttpClient.Builder()

        logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        if (BuildConfig.DEBUG) {
            okhttpClientBuilder.addInterceptor(logging)
        }

        mapper = ObjectMapper()
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        mapper.visibilityChecker = VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY)

        return Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okhttpClientBuilder.build())
                .build()
    }
}