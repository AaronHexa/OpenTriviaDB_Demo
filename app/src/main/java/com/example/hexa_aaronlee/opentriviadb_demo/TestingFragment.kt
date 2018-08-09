package com.example.hexa_aaronlee.opentriviadb_demo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hexa_aaronlee.opentriviadb_demo.API.TokenAPI
import com.example.hexa_aaronlee.opentriviadb_demo.Object_and_Data.TokenData
import com.example.hexa_aaronlee.opentriviadb_demo.Object_and_Data.TokenObject
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.introspect.VisibilityChecker
import com.fasterxml.jackson.databind.DeserializationFeature
import java.lang.Compiler.disable
import com.fasterxml.jackson.databind.ObjectMapper




class TestingFragment : Fragment() {

    lateinit var jsonApi: TokenAPI
    lateinit var mObservable: Observable<TokenObject>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_testing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* val mapper = ObjectMapper()
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        mapper.visibilityChecker = VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY)*/

        val retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        jsonApi = retrofit.create(TokenAPI::class.java)

        mObservable = jsonApi.getToken()

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<TokenObject>
                {
                    override fun onNext(t: TokenObject) {
                        Log.i("data", t.data!![0].token)
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Log.e("Error",e.toString())
                    }

                })

    }



    /*fun getApiService(enableHTTPLogging: Boolean?): ApiService {
        if (ApiService == null) {
            Logger.info("No instance of service yet. Building...")
            val objectMapper = ObjectMapper().registerModule(Jdk8Module())
            val retrofitBuilder = Retrofit.Builder()
                    .baseUrl("https://api.telegram.org/bot$Token/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            if (enableHTTPLogging!!) {
                Logger.info("Adding request logging...")
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                val httpClient = OkHttpClient.Builder()
                httpClient.addInterceptor(httpLoggingInterceptor)
                retrofitBuilder.client(httpClient.build())
            }
            val retrofit = retrofitBuilder.build()
            ApiService = retrofit.create<TelegramBotApiService>(TelegramBotApiService::class.java!!)
        }
        Logger.debug("Returning ApiService instance.")
        return ApiService
    }*/
}
