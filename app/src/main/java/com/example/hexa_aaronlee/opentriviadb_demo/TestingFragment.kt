package com.example.hexa_aaronlee.opentriviadb_demo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hexa_aaronlee.opentriviadb_demo.API.*
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.introspect.VisibilityChecker
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper


class TestingFragment : Fragment() {

    lateinit var jsonApi: TokenAPI
    lateinit var resetApi: ResetApi
    lateinit var mObservable: Observable<TokenData>
    lateinit var mToken : String
    var token_response_code : Long = 0
    lateinit var mapper: ObjectMapper
    lateinit var retrofit : Retrofit
    lateinit var mQuestionCountApi: QuestionCountApi
    lateinit var mCategoryApi: CategoryApi
    lateinit var mQuestionApi: QuestionApi

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_testing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapper = ObjectMapper()
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        mapper.visibilityChecker = VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY)

        retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        //RequestToken()

        //ResetToken()

        //RequestCategory()

        //RequestAllQuestionCount()

        RequestQuestion()
    }

    fun RequestToken(){

        jsonApi = retrofit.create(TokenAPI::class.java)

        mObservable = jsonApi.getToken()

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<TokenData>
                {
                    override fun onNext(t: TokenData) {
                        Log.i("Get Data", "${t.token} / ${t.response_code}")
                        token_response_code = t.response_code
                        mToken = t.token
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

    fun ResetToken(){
        mToken = "ac7e4f8a320d72f9ed3c9a1b416e54915fce0ea88208aac0e1d35455611bb52f"
        val resetUrl = "api_token.php?command=reset&token="

        resetApi = retrofit.create(ResetApi::class.java)

        val mObservable = resetApi.resetToken(resetUrl+mToken)

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResetData>
                {
                    override fun onNext(t: ResetData) {
                        Log.i("Get Data", "${t.token} / ${t.response_code}")
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

    fun RequestCategory(){
        mCategoryApi = retrofit.create(CategoryApi::class.java)

        val mObservable = mCategoryApi.getAllCategoryData()

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CategoryData>
                {
                    override fun onNext(t: CategoryData) {
                        Log.i("Get Data", "${t.triviaCategories[0].name} / ${t.triviaCategories[0].id}")
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

    fun RequestAllQuestionCount(){
        mQuestionCountApi = retrofit.create(QuestionCountApi::class.java)

        val mObservable = mQuestionCountApi.getQuestionCountData()

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<QuestionCountData>
                {
                    override fun onNext(t: QuestionCountData) {
                        Log.i("Get Data", "overall : ${t.overall.totalNumOfVerifiedQuestions} / category id 9 : ${t.categories.categoryItem9.totalNumOfVerifiedQuestions}")
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


    fun RequestQuestion(){
        mToken = "ac7e4f8a320d72f9ed3c9a1b416e54915fce0ea88208aac0e1d35455611bb52f"
        val questionUrl = "api.php?amount=1&category=30&token="

        //&category=__ (selected category id)

        //&difficulty=__ (easy,medium,hard)

        //Type of answer only multiple or boolean
        //&type=__ (multiple/ boolean)

        mQuestionApi = retrofit.create(QuestionApi::class.java)

        val mObservable = mQuestionApi.getSelectedQuestion(questionUrl+mToken)

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<QuestionData>
                {
                    override fun onNext(t: QuestionData) {
                        Log.i("Get Data", "Response Code : ${t.responseCode} / Question Category : ${t.results[0].category}")
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
}
