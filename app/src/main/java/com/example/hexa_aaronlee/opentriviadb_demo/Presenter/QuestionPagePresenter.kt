package com.example.hexa_aaronlee.opentriviadb_demo.Presenter

import android.support.constraint.ConstraintLayout
import android.util.Log
import com.example.hexa_aaronlee.opentriviadb_demo.API.QuestionApi
import com.example.hexa_aaronlee.opentriviadb_demo.API.ResetApi
import com.example.hexa_aaronlee.opentriviadb_demo.API.TokenAPI
import com.example.hexa_aaronlee.opentriviadb_demo.Model.RetrofitApi
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.QuestionData
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.ResetData
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.TokenData
import com.example.hexa_aaronlee.opentriviadb_demo.RealmObject.TokenInfoData
import com.example.hexa_aaronlee.opentriviadb_demo.View.QuestionPageView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import retrofit2.Retrofit
import java.util.*

class QuestionPagePresenter(internal val mView: QuestionPageView.View) : QuestionPageView.Presenter {

    lateinit var retrofit: Retrofit

    lateinit var mTokenAPI: TokenAPI
    lateinit var mResetApi: ResetApi
    lateinit var mQuestionApi: QuestionApi
    private var tmpResponseCode = 0
    private var newToken = ""
    lateinit var RetrofitApiModel: RetrofitApi

    override fun setRetrofitAndMapper() {

        RetrofitApiModel = RetrofitApi()
        retrofit = RetrofitApiModel.RequestRetrofitApi()
    }

    override fun checkTokenAvailable(token: String) {

        val tmpUrl = "api.php?amount=1&token=$token"

        mQuestionApi = retrofit.create(QuestionApi::class.java)

        val mObservable = mQuestionApi.getSelectedQuestion(tmpUrl)

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<QuestionData> {
                    override fun onNext(t: QuestionData) {
                        tmpResponseCode = t.responseCode

                        when (tmpResponseCode) {
                            4 -> resetToken(token)
                            3 -> requestNewToken()
                            else -> mView.getRequirementQuestion()
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        mView.showRetrieveDataError(e.message!!, "Check Token")
                    }

                })
    }

    fun resetToken(token: String) {
        val resetUrl = "api_token.php?command=reset&token=$token"

        mResetApi = retrofit.create(ResetApi::class.java)

        val mObservable = mResetApi.resetToken(resetUrl)

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResetData> {
                    override fun onNext(t: ResetData) {
                        tmpResponseCode = t.response_code.toInt()
                    }

                    override fun onComplete() {
                        if (tmpResponseCode == 0) {
                            mView.showResetDone()
                        } else if (tmpResponseCode == 3) {
                            requestNewToken()
                        }
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        mView.showRetrieveDataError(e.message!!, "Reset Token")
                    }

                })
    }

    fun requestNewToken() {

        mTokenAPI = retrofit.create(TokenAPI::class.java)

        val mObservable = mTokenAPI.getToken()

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<TokenData> {
                    override fun onNext(t: TokenData) {
                        newToken = t.token
                        //Log.i("New Token",t.token.toString())
                    }

                    override fun onComplete() {
                        mView.saveNewToken(newToken)

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        mView.showRetrieveDataError(e.message!!, "Create Token")
                    }

                })
    }

    override fun saveInRealmDB(myRealm: Realm, token: String) {
        val result = myRealm.where(TokenInfoData::class.java).findAllAsync()
        result.forEach {
            it.token = token
        }
    }

    override fun requestQuestionData(token: String, category: String, difficulty: String, typeQuestion: String) {

        var categoryUrl = ""
        var difficultyUrl = ""
        var typeQuestionUrl = ""

        //Question
        var questionTxt = ""
        var difficultyQuestion = ""
        val answerArray = ArrayList<String>()
        var correctAnswer = ""
        var type = ""

        if (category != "0") {
            categoryUrl = "&category=$category"
        } else if (category == "0") {
            categoryUrl = ""
        }

        if (difficulty != "default") {
            difficultyUrl = "&difficulty=$difficulty"
        } else if (difficulty == "default") {
            difficultyUrl = ""
        }

        if (typeQuestion != "default") {
            typeQuestionUrl = "&type=$typeQuestion"
        } else if (typeQuestion == "default") {
            typeQuestionUrl = ""
        }

        val tmpUrl = "api.php?amount=1$categoryUrl$difficultyUrl$typeQuestionUrl&token=$token"

        mQuestionApi = retrofit.create(QuestionApi::class.java)

        val mObservable = mQuestionApi.getSelectedQuestion(tmpUrl)
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<QuestionData> {
                    override fun onNext(t: QuestionData) {

                        tmpResponseCode = t.responseCode

                        //Log.i("bblbll", tmpResponseCode.toString())
                        when (tmpResponseCode) {
                            0 -> {
                                questionTxt = t.results[0].question
                                difficultyQuestion = t.results[0].difficulty
                                correctAnswer = t.results[0].correctAnswer
                                answerArray.add(t.results[0].correctAnswer)
                                type = t.results[0].type

                                for (i in t.results[0].incorrectAnswers.indices) {
                                    answerArray.add(t.results[0].incorrectAnswers[i])
                                }

                                answerArray.shuffle()
                            }
                            3 -> {
                                requestNewToken()
                            }
                            4 -> {
                                resetToken(token)
                            }
                        }
                    }

                    override fun onComplete() {
                        mView.updateQuestionToUI(questionTxt, difficultyQuestion, correctAnswer, answerArray, type)
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        mView.showRetrieveDataError(e.message!!, "Get Question")
                    }

                })
    }

    override fun checkCorrectAnswer(correctAnswer: String, selectedAnswer: String, answerLayout: ConstraintLayout) {
        if (selectedAnswer == correctAnswer) {
            mView.answerIsCorrect(answerLayout)
        } else {
            mView.answerIsWrong(answerLayout)
        }
    }


}