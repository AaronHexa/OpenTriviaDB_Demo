package com.example.hexa_aaronlee.opentriviadb_demo.Presenter

import android.R
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.example.hexa_aaronlee.opentriviadb_demo.API.CategoryApi
import com.example.hexa_aaronlee.opentriviadb_demo.API.TokenAPI
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.CategoryData
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.TokenData
import com.example.hexa_aaronlee.opentriviadb_demo.RealmObject.TokenInfoData
import com.example.hexa_aaronlee.opentriviadb_demo.View.MainPageView
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.introspect.VisibilityChecker
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_main_page.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.ArrayList

class MainPagePresenter(internal val myView: MainPageView.View) : MainPageView.Presenter {

    lateinit var mCategoryApi: CategoryApi
    lateinit var mapper: ObjectMapper
    lateinit var retrofit: Retrofit
    lateinit var mTokenAPI: TokenAPI

    override fun RequestCategory(mView: View, categoryArray: ArrayList<String>, categoryIdArray: ArrayList<Long>, difficultyArray: ArrayList<String>, typeQuestionArray: ArrayList<String>) {

        /*val okHttpClient = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        okHttpClient.addInterceptor(logging)*/

        mapper = ObjectMapper()
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        mapper.visibilityChecker = VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY)

        retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

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

        mapper = ObjectMapper()
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        mapper.visibilityChecker = VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY)

        retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

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