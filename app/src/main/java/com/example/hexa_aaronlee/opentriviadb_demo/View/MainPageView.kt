package com.example.hexa_aaronlee.opentriviadb_demo.View

import android.view.View
import android.widget.ArrayAdapter
import com.example.hexa_aaronlee.opentriviadb_demo.SharedPreference.MySharedPreference
import io.realm.Realm
import java.util.ArrayList

interface MainPageView {
    interface View{
        fun hideLoadingIndicator()
        fun setAdapterSpinner(categoryArray: ArrayList<String>,
                              difficultyArray: ArrayList<String>,
                              typeQuestionArray: ArrayList<String>)

        fun saveTokenInSharedPreferrence(token: String)

        fun showRetrieveDataError(error: String, title: String)

        fun successfullySave()
    }

    interface Presenter {
        fun requestCategory(mView: android.view.View,
                            categoryArray: ArrayList<String>,
                            categoryIdArray: ArrayList<Long>,
                            difficultyArray: ArrayList<String>,
                            typeQuestionArray: ArrayList<String>)

        fun checkTokenExistInSharePreference(mySharedPreference: MySharedPreference)

    }
}