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
    lateinit var mCategoryArray: ArrayList<CategoryData>
    lateinit var mMutableQuestionCount : MutableMap<String,QuestionCountArray>

    override fun setRetrofitAndMapper(myView: View) {

        RetrofitApiModel = RetrofitApi()
        retrofit = RetrofitApiModel.requestRetrofitApi()
    }

    override fun RequestGetCategory() {

        mCategoryArray = ArrayList()

        mApiServices = retrofit.create(ApiServices::class.java)

        val mObservable = mApiServices.getAllCategoryData()

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CategoryData> {
                    override fun onNext(t: CategoryData) {
                        mCategoryArray.add(t)
                        Log.i("Get Data", mCategoryArray[0].triviaCategories.size.toString())

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

        mMutableQuestionCount = mutableMapOf()

        var tmpCount = 0

        mApiServices = retrofit.create(ApiServices::class.java)

        for (i in mCategoryArray[0].triviaCategories.indices) {

            val categoryId = mCategoryArray[0].triviaCategories[i].id

            val dataUrl = "api_count.php?category=$categoryId"

            val mObservable = mApiServices.getQuestionCountData(dataUrl)

            mObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<QuestionCountData> {
                        override fun onNext(t: QuestionCountData) {
                            val data = QuestionCountArray(t.categoryQuestionCount.totalCount,t.categoryQuestionCount.easyCount,
                                    t.categoryQuestionCount.mediumCount,t.categoryQuestionCount.hardCount,categoryId.toString())

                            mMutableQuestionCount["$i"] = data
                            Log.i("Get Count", " ${t.categoryQuestionCount.totalCount}; id = $categoryId : " +
                                    "${t.categoryQuestionCount.easyCount}}/ ")
                        }

                        override fun onComplete() {

                            tmpCount += 1

                            if (tmpCount == mCategoryArray[0].triviaCategories.size - 1) {
                                if (mView != null) {
                                    mView?.setListView(mMutableQuestionCount,mCategoryArray)

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