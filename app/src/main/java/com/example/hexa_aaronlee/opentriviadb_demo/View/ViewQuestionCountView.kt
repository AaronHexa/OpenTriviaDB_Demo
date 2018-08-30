package com.example.hexa_aaronlee.opentriviadb_demo.View

import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.CategoryData
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.QuestionCountArray

interface ViewQuestionCountView {
    interface View {
        fun setListView(mMutableQuestionCount : MutableMap<String , QuestionCountArray>,
                        mCategoryList : ArrayList<CategoryData>)

        fun showErrorGetData(e: String)


    }

    interface Presenter {
        fun setRetrofitAndMapper(myView: android.view.View)

        fun RequestGetCategory()
        fun onDestroy()
    }
}