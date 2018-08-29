package com.example.hexa_aaronlee.opentriviadb_demo.Presenter

import android.util.Log
import android.view.View
import com.example.hexa_aaronlee.opentriviadb_demo.API.ApiServices
import com.example.hexa_aaronlee.opentriviadb_demo.Model.RetrofitApi
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.CategoryData
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.TokenData
import com.example.hexa_aaronlee.opentriviadb_demo.SharedPreference.MySharedPreference
import com.example.hexa_aaronlee.opentriviadb_demo.View.MainPageView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.util.ArrayList

class MainPagePresenter(internal var myView: MainPageView.View?) : MainPageView.Presenter {

    lateinit var mApiServices : ApiServices
    lateinit var retrofit: Retrofit
    lateinit var retrofitApiModel: RetrofitApi

    override fun requestCategory(mView: View, categoryArray: ArrayList<String>, categoryIdArray: ArrayList<Long>, difficultyArray: ArrayList<String>, typeQuestionArray: ArrayList<String>) {

        retrofitApiModel = RetrofitApi()
        retrofit = retrofitApiModel.requestRetrofitApi()

        mApiServices = retrofit.create(ApiServices::class.java)

        val mObservable = mApiServices.getAllCategoryData()

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CategoryData> {
                    override fun onNext(t: CategoryData) {
                        for (i in 0 until t.triviaCategories.size) {
                            categoryArray.add(t.triviaCategories[i].name)
                            categoryIdArray.add(t.triviaCategories[i].id)
                            //Log.i("Get Data", "${categoryArray[i]} / ${categoryIdArray[i]}")
                        }

                    }

                    override fun onComplete() {
                        myView?.setAdapterSpinner(categoryArray, difficultyArray, typeQuestionArray)
                        myView?.hideLoadingIndicator()

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        checkNullError(e, "Spinner Data")
                    }

                })
    }

    override fun checkTokenExistInSharePreference(mySharedPreference: MySharedPreference) {

        val tokenExist = mySharedPreference.checkToken()


        if (!tokenExist) {

            requestNewTokenCode()

        }

    }

    fun requestNewTokenCode() {

        var newToken = ""

        retrofitApiModel = RetrofitApi()
        retrofit = retrofitApiModel.requestRetrofitApi()

        mApiServices = retrofit.create(ApiServices::class.java)

        val mObservable = mApiServices.getToken()

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<TokenData> {
                    override fun onNext(t: TokenData) {
                        newToken = t.token
                        Log.i("Data Key", t.token)
                    }

                    override fun onComplete() {
                        myView?.saveTokenInSharedPreferrence(newToken)

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        checkNullError(e, "Create Token")
                    }

                })
    }

    fun checkNullError(error: Throwable, errorPartName: String) {

        val errorMsg = error.message ?: "Error Throwable Message is Null"
        myView?.showRetrieveDataError(errorMsg, errorPartName)

    }
}