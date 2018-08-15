package com.example.hexa_aaronlee.opentriviadb_demo.Fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.hexa_aaronlee.opentriviadb_demo.API.CategoryApi
import com.example.hexa_aaronlee.opentriviadb_demo.API.QuestionCountApi
import com.example.hexa_aaronlee.opentriviadb_demo.Adapter.QuestionCountAdapter
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.CategoryData
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.QuestionCountData
import com.example.hexa_aaronlee.opentriviadb_demo.R
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.introspect.VisibilityChecker
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_view_question_page.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.*
import kotlin.collections.ArrayList


class ViewQuestionPageFragment : Fragment() {

    lateinit var mapper: ObjectMapper
    lateinit var retrofit: Retrofit
    lateinit var mQuestionCountApi: QuestionCountApi
    lateinit var mQuestionCountArray: ArrayList<Int>
    lateinit var mEasyCountArray: ArrayList<Int>
    lateinit var mMediumCountArray: ArrayList<Int>
    lateinit var mHardCountArray: ArrayList<Int>
    lateinit var mCategoryArray: ArrayList<String>
    lateinit var mCategoryIdArray: ArrayList<String>

    lateinit var mCategoryApi: CategoryApi

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_question_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mQuestionCountArray = ArrayList()
        mEasyCountArray = ArrayList()
        mMediumCountArray = ArrayList()
        mHardCountArray = ArrayList()
        mCategoryArray = ArrayList()
        mCategoryIdArray = ArrayList()

        loadingListData.visibility = View.VISIBLE

        mapper = ObjectMapper()
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        mapper.visibilityChecker = VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY)

        retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        listViewCount.setHasFixedSize(true)
        listViewCount.layoutManager = LinearLayoutManager(view.context)

        RequestCategory()
    }

    fun RequestCategory() {

        mCategoryApi = retrofit.create(CategoryApi::class.java)

        val mObservable = mCategoryApi.getAllCategoryData()

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CategoryData> {
                    override fun onNext(t: CategoryData) {
                        for (i in 0 until t.triviaCategories.size) {
                            mCategoryArray.add(t.triviaCategories[i].name)
                            mCategoryIdArray.add(t.triviaCategories[i].id.toString())
                            Log.i("Get Data", mCategoryArray[i])
                        }

                    }

                    override fun onComplete() {
                        Log.i("Get Data", "Complete")
                        RequestAllQuestionCount()
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Log.e("Error", e.toString())
                    }

                })
    }

    fun RequestAllQuestionCount() {

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
                            Log.i("Get Count", " ${mCategoryArray.size} = $tmpCount : ${t.categoryQuestionCount.totalCount} / category id 9 : ${t.categoryQuestionCount.easyCount}")
                        }

                        override fun onComplete() {
                            tmpCount += 1

                            if (tmpCount == mCategoryArray.size - 1) {
                                Log.i("Pass by Count", "yes")
                                loadingListData.visibility = View.INVISIBLE
                                setListView()
                            }
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onError(e: Throwable) {
                            Log.e("Error", e.toString())
                        }

                    })
        }
    }

    fun setListView() {
        val myAdapter = QuestionCountAdapter(view!!.context, mQuestionCountArray, mCategoryArray, mEasyCountArray, mMediumCountArray, mHardCountArray)
        listViewCount.adapter = myAdapter
    }
}
