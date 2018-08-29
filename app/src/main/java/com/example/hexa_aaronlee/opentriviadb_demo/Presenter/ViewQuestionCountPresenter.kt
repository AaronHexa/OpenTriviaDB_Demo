package com.example.hexa_aaronlee.opentriviadb_demo.Presenter

import android.util.Log
import android.view.View
import com.example.hexa_aaronlee.opentriviadb_demo.API.ApiServices
import com.example.hexa_aaronlee.opentriviadb_demo.Model.RetrofitApi
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.CategoryData
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.QuestionCountArray
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.QuestionCountData
import com.example.hexa_aaronlee.opentriviadb_demo.View.ViewQuestionCountView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class ViewQuestionCountPresenter(internal var mView: ViewQuestionCountView.View?) : ViewQuestionCountView.Presenter {

    lateinit var retrofit: Retrofit
    lateinit var mApiServices: ApiServices
    lateinit var RetrofitApiModel: RetrofitApi

    override fun setRetrofitAndMapper(myView: View) {

        RetrofitApiModel = RetrofitApi()
        retrofit = RetrofitApiModel.requestRetrofitApi()
    }

    override fun RequestGetCategory() {

        mApiServices = retrofit.create(ApiServices::class.java)

        val mObservable = mApiServices.getAllCategoryData()

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CategoryData> {
                    override fun onNext(t: CategoryData) {
                        for (i in 0 until t.triviaCategories.size) {
                            QuestionCountArray.mCategoryArray.add(t.triviaCategories[i].name)
                            QuestionCountArray.mCategoryIdArray.add(t.triviaCategories[i].id.toString())
                            Log.i("Get Data", QuestionCountArray.mCategoryIdArray[i])
                        }

                    }

                    override fun onComplete() {
                        RequestAllQuestionCount()
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        checkErrorIsNull(e)
                    }

                })
    }

    fun RequestAllQuestionCount() {

        var tmpCount = 0

        mApiServices = retrofit.create(ApiServices::class.java)

        for (i in QuestionCountArray.mCategoryArray.indices) {

            Log.i("count" , i.toString())

            val dataUrl = "api_count.php?category=${QuestionCountArray.mCategoryIdArray[i]}"

            val mObservable = mApiServices.getQuestionCountData(dataUrl)

            mObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<QuestionCountData> {
                        override fun onNext(t: QuestionCountData) {
                            QuestionCountArray.mCountArray.add(t.categoryQuestionCount.totalCount)
                            QuestionCountArray.mEasyCountArray.add(t.categoryQuestionCount.easyCount)
                            QuestionCountArray.mMediumCountArray.add(t.categoryQuestionCount.mediumCount)
                            QuestionCountArray.mHardCountArray.add(t.categoryQuestionCount.hardCount)
                            Log.i("Get Count", " ${t.categoryQuestionCount.totalCount}; id = ${QuestionCountArray.mCategoryIdArray[i]} : " +
                                    "${t.categoryQuestionCount.easyCount}}/ $i")
                        }

                        override fun onComplete() {

                            tmpCount += 1

                            if (tmpCount == QuestionCountArray.mCategoryArray.size - 1) {
                                if (mView != null) {
                                    mView?.setListView()

                                }

                            }
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onError(e: Throwable) {
                            checkErrorIsNull(e)
                        }

                    })
        }
    }

    override fun onDestroy() {
        mView = null
    }

    fun checkErrorIsNull(error: Throwable) {
        val errorMsg = error.message ?: "Error Throwable Message is Null"
        mView?.showErrorGetData(errorMsg)
    }
}