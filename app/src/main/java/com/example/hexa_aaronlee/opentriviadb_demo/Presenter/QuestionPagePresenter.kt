package com.example.hexa_aaronlee.opentriviadb_demo.Presenter

import android.support.constraint.ConstraintLayout
import android.util.Log
import com.example.hexa_aaronlee.opentriviadb_demo.API.ApiServices
import com.example.hexa_aaronlee.opentriviadb_demo.Model.RetrofitApi
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.GetDetailQuestionData
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

class QuestionPagePresenter(internal var mView: QuestionPageView.View?) : QuestionPageView.Presenter {

    lateinit var retrofit: Retrofit

    lateinit var mApiServices: ApiServices
    private var tmpResponseCode = 0
    private var newToken = ""
    lateinit var RetrofitApiModel: RetrofitApi
    lateinit var dataQuestion : GetDetailQuestionData

    override fun setRetrofitAndMapper() {

        RetrofitApiModel = RetrofitApi()
        retrofit = RetrofitApiModel.requestRetrofitApi()
    }

    override fun checkTokenAvailable(token: String) {

        val tmpUrl = "api.php?amount=1&token=$token"

        mApiServices = retrofit.create(ApiServices::class.java)

        val mObservable = mApiServices.getSelectedQuestion(tmpUrl)

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<QuestionData> {
                    override fun onNext(t: QuestionData) {
                        tmpResponseCode = t.responseCode

                        when (tmpResponseCode) {
                            4 -> resetToken(token)
                            3 -> requestNewToken()
                            else -> {
                                if (mView != null) {
                                    mView?.getRequirementQuestion()
                                }
                            }
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        checkErrorIsNull(e, "Check Token")
                    }

                })
    }

    fun resetToken(token: String) {
        val resetUrl = "api_token.php?command=reset&token=$token"

        mApiServices = retrofit.create(ApiServices::class.java)

        val mObservable = mApiServices.resetToken(resetUrl)

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResetData> {
                    override fun onNext(t: ResetData) {
                        tmpResponseCode = t.response_code.toInt()
                    }

                    override fun onComplete() {
                        if (tmpResponseCode == 0) {
                            if (mView != null) {
                                mView?.showResetDone()
                            }
                        } else if (tmpResponseCode == 3) {
                            requestNewToken()
                        }
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        checkErrorIsNull(e, "Reset Token")
                    }

                })
    }

    fun requestNewToken() {

        mApiServices = retrofit.create(ApiServices::class.java)

        val mObservable = mApiServices.getToken()

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<TokenData> {
                    override fun onNext(t: TokenData) {
                        newToken = t.token
                        Log.i("New Token", t.token.toString())
                    }

                    override fun onComplete() {
                        if (mView != null) {
                            mView?.saveNewToken(newToken)
                        }
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        checkErrorIsNull(e, "Create Token")
                    }

                })
    }

    override fun saveInRealmDB(myRealm: Realm, token: String) {
        val result = myRealm.where(TokenInfoData::class.java).findAllAsync()
        result.forEach {
            myRealm.beginTransaction()
            it.token = token
            myRealm.commitTransaction()
            //Log.i("realm token", "${it.token} ///  $token")
        }
    }

    override fun requestQuestionData(token: String, category: String, difficulty: String, typeQuestion: String) {

        var categoryUrl = ""
        var difficultyUrl = ""
        var typeQuestionUrl = ""

        //Question
        dataQuestion = GetDetailQuestionData()

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

        mApiServices = retrofit.create(ApiServices::class.java)

        val mObservable = mApiServices.getSelectedQuestion(tmpUrl)
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<QuestionData> {
                    override fun onNext(t: QuestionData) {

                        tmpResponseCode = t.responseCode

                        //Log.i("bblbll", tmpResponseCode.toString())
                        when (tmpResponseCode) {
                            0 -> {
                                dataQuestion.questionTxt = t.results[0].question
                                dataQuestion.difficultyQuestion = t.results[0].difficulty
                                dataQuestion.correctAnswer = t.results[0].correctAnswer
                                dataQuestion.answerArray.add(t.results[0].correctAnswer)
                                dataQuestion.type = t.results[0].type

                                for (i in t.results[0].incorrectAnswers.indices) {
                                    dataQuestion.answerArray.add(t.results[0].incorrectAnswers[i])
                                }

                                dataQuestion.answerArray.shuffle()
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
                        if (mView != null) {
                            mView?.updateQuestionToUI(dataQuestion)
                        }

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        checkErrorIsNull(e, "Get Question")
                    }
                })
    }

    override fun checkCorrectAnswer(correctAnswer: String, selectedAnswer: String, answerLayout: ConstraintLayout) {
        if (selectedAnswer == correctAnswer) {
            if (mView != null) {
                mView?.answerIsCorrect(answerLayout)
            }

        } else {
            if (mView != null) {
                mView?.answerIsWrong(answerLayout)
            }
        }
    }

    override fun onDestroy() {
        mView = null

        /*val newView = WeakReference<View>(mView)
        newView.clear()*/
    }

    fun checkErrorIsNull(error: Throwable, errorPartName: String) {
        val errorMsg = error.message ?: "Error Throwable Message is Null"
        mView?.showRetrieveDataError(errorMsg, errorPartName)
    }

}