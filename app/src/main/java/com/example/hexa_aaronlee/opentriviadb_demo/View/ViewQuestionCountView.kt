package com.example.hexa_aaronlee.opentriviadb_demo.View

import android.view.View
import com.example.hexa_aaronlee.opentriviadb_demo.API.CategoryApi

interface ViewQuestionCountView {
    interface View {
        fun setListView(mQuestionCountArray: ArrayList<Int>,
                        mCategoryArray: ArrayList<String>,
                        mEasyCountArray: ArrayList<Int>,
                        mMediumCountArray: ArrayList<Int>,
                        mHardCountArray: ArrayList<Int>)

        fun showErrorGetData(e: String)
    }

    interface Presenter {
        fun setRetrofitAndMapper(myView: android.view.View)

        fun RequestGetCategory(mCategoryArray: ArrayList<String>,
                               mCategoryIdArray: ArrayList<String>,
                               mQuestionCountArray: ArrayList<Int>,
                               mEasyCountArray: ArrayList<Int>,
                               mMediumCountArray: ArrayList<Int>,
                               mHardCountArray: ArrayList<Int>)
        fun onDestroy()
    }
}