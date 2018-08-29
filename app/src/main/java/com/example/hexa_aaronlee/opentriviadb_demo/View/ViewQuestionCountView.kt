package com.example.hexa_aaronlee.opentriviadb_demo.View

import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.QuestionCountArray

interface ViewQuestionCountView {
    interface View {
        fun setListView()

        fun showErrorGetData(e: String)
    }

    interface Presenter {
        fun setRetrofitAndMapper(myView: android.view.View)

        fun RequestGetCategory()
        fun onDestroy()
    }
}