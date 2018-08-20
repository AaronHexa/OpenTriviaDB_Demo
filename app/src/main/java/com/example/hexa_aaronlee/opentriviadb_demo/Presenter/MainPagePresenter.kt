package com.example.hexa_aaronlee.opentriviadb_demo.Presenter

import android.R
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.example.hexa_aaronlee.opentriviadb_demo.API.CategoryApi
import com.example.hexa_aaronlee.opentriviadb_demo.API.TokenAPI
import com.example.hexa_aaronlee.opentriviadb_demo.Model.RetrofitApi
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.CategoryData
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.TokenData
import com.example.hexa_aaronlee.opentriviadb_demo.RealmObject.TokenInfoData
import com.example.hexa_aaronlee.opentriviadb_demo.View.MainPageView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import retrofit2.Retrofit
import java.util.ArrayList

class MainPagePresenter(internal val myView: MainPageView.View) : MainPageView.Presenter {

    lateinit var mCategoryApi: CategoryApi
    lateinit var retrofit: Retrofit
    lateinit var mTokenAPI: TokenAPI
    lateinit var RetrofitApiModel: RetrofitApi

    override fun RequestCategory(mView: View, categoryArray: ArrayList<String>, categoryIdArray: ArrayList<Long>, difficultyArray: ArrayList<String>, typeQuestionArray: ArrayList<String>) {

        RetrofitApiModel = RetrofitApi()
        retrofit = RetrofitApiModel.RequestRetrofitApi()

        mCategoryApi = retrofit.create(CategoryApi::class.java)

        val mObservable = mCategoryApi.getAllCategoryData()

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
                        setDataSpinner(mView, categoryArray, difficultyArray, typeQuestionArray)
                        myView.hideLoadingIndicator()

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        myView.showRetrieveDataError(e.message!!, "Spinner Data")

                    }

                })
    }

    fun setDataSpinner(mView: View, categoryArray: ArrayList<String>, difficultyArray: ArrayList<String>, typeQuestionArray: ArrayList<String>) {
        val categoryAdapter = ArrayAdapter(mView.context, R.layout.simple_spinner_item, categoryArray)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val difficultyAdapter = ArrayAdapter(mView.context, R.layout.simple_spinner_item, difficultyArray)
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        val typeQuestionAdapter = ArrayAdapter(mView.context, R.layout.simple_spinner_item, typeQuestionArray)
        typeQuestionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        //Setting the ArrayAdapter data on the Spinner
        myView.setAdapterSpinner(categoryAdapter, difficultyAdapter, typeQuestionAdapter)
    }

    override fun checkTokenExistInRealm(myRealm: Realm) {
        val tokenExist = myRealm.where(TokenInfoData::class.java).findFirstAsync()

        if (tokenExist != null) {
            requestNewTokenCode()
        } else {
            val result = myRealm.where(TokenInfoData::class.java).findAllAsync()
            result.forEach {
                myView.saveTokenInSharedPreferrence(it.token, 0)
            }

        }
    }

    fun requestNewTokenCode() {

        var newToken = ""

        RetrofitApiModel = RetrofitApi()
        retrofit = RetrofitApiModel.RequestRetrofitApi()

        mTokenAPI = retrofit.create(TokenAPI::class.java)

        val mObservable = mTokenAPI.getToken()

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<TokenData> {
                    override fun onNext(t: TokenData) {
                        newToken = t.token
                    }

                    override fun onComplete() {
                        myView.saveTokenInSharedPreferrence(newToken, 1)

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        myView.showRetrieveDataError(e.message!!, "Create Token")
                    }

                })
    }

    override fun saveInRealmDB(myRealm: Realm, token: String) {
        myRealm.executeTransactionAsync({ bgRealm ->
            val user = bgRealm.createObject(TokenInfoData::class.java)
            user.token = token

        }, {
            myView.successfullySave()
        }, {
            myView.showRetrieveDataError(it.message!!, "Error Realm")
        })
    }
}