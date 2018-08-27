package com.example.hexa_aaronlee.opentriviadb_demo.Presenter

import android.util.Log
import android.view.View
import com.example.hexa_aaronlee.opentriviadb_demo.API.CategoryApi
import com.example.hexa_aaronlee.opentriviadb_demo.API.QuestionCountApi
import com.example.hexa_aaronlee.opentriviadb_demo.Model.RetrofitApi
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.CategoryData
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.QuestionCountData
import com.example.hexa_aaronlee.opentriviadb_demo.View.ViewQuestionCountView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class ViewQuestionCountPresenter(internal var mView: ViewQuestionCountView.View?) : ViewQuestionCountView.Presenter {

    lateinit var retrofit: Retrofit
    lateinit var myCategoryApi: CategoryApi
    lateinit var mQuestionCountApi: QuestionCountApi
    lateinit var RetrofitApiModel: RetrofitApi

    override fun setRetrofitAndMapper(myView: View) {

        RetrofitApiModel = RetrofitApi()
        retrofit = RetrofitApiModel.RequestRetrofitApi()
    }

    override fun RequestGetCategory(mCategoryArray: ArrayList<String>,
                                    mCategoryIdArray: ArrayList<String>,
                                    mQuestionCountArray: ArrayList<Int>,
                                    mEasyCountArray: ArrayList<Int>,
                                    mMediumCountArray: ArrayList<Int>,
                                    mHardCountArray: ArrayList<Int>) {

        myCategoryApi = retrofit.create(CategoryApi::class.java)

        val mObservable = myCategoryApi.getAllCategoryData()

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CategoryData> {
                    override fun onNext(t: CategoryData) {
                        for (i in 0 until t.triviaCategories.size) {
                            mCategoryArray.add(t.triviaCategories[i].name)
                            mCategoryIdArray.add(t.triviaCategories[i].id.toString())
                            //Log.i("Get Data", mCategoryArray[i])
                        }

                    }

                    override fun onComplete() {
                        RequestAllQuestionCount(mCategoryArray, mCategoryIdArray, mQuestionCountArray, mEasyCountArray, mMediumCountArray, mHardCountArray)
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        if (mView != null){
                            mView!!.showErrorGetData(e.message!!)
                        }
                    }

                })
    }

    fun RequestAllQuestionCount(mCategoryArray: ArrayList<String>,
                                mCategoryIdArray: ArrayList<String>,
                                mQuestionCountArray: ArrayList<Int>,
                                mEasyCountArray: ArrayList<Int>,
                                mMediumCountArray: ArrayList<Int>,
                                mHardCountArray: ArrayList<Int>) {

        var tmpCount = 0

        mQuestionCountApi = retrofit.create(QuestionCountApi::class.java)

        for (i in 0 until mCategoryArray.size) {

            val dataUrl = "api_count.php?category=${mCategoryIdArray[i]}"

            val mObservable = mQuestionCountApi.getQuestionCountData(dataUrl)

            mObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<QuestionCountData> {
                        override fun onNext(t: QuestionCountData) {
                            mQuestionCountArray.add(t.categoryQuestionCount.totalCount)
                            mEasyCountArray.add(t.categoryQuestionCount.easyCount)
                            mMediumCountArray.add(t.categoryQuestionCount.mediumCount)
                            mHardCountArray.add(t.categoryQuestionCount.hardCount)
                            //Log.i("Get Count", " ${mCategoryArray.size} = $tmpCount : ${t.categoryQuestionCount.totalCount} / category id 9 : ${t.categoryQuestionCount.easyCount}")
                        }

                        override fun onComplete() {
                            tmpCount += 1

                            if (tmpCount == mCategoryArray.size - 1) {
                                if (mView != null) {
                                    mView!!.setListView(mQuestionCountArray, mCategoryArray, mEasyCountArray, mMediumCountArray, mHardCountArray)

                                }
                            }
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onError(e: Throwable) {
                            if (mView != null){
                                mView!!.showErrorGetData(e.message!!)
                            }
                        }

                    })
        }
    }

    override fun onDestroy() {
        mView = null
    }

}