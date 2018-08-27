package com.example.hexa_aaronlee.opentriviadb_demo.View

import android.view.View
import android.widget.ArrayAdapter
import io.realm.Realm
import java.util.ArrayList

interface MainPageView {
    interface View{
        fun hideLoadingIndicator()
        fun setAdapterSpinner(categoryAdapter: ArrayAdapter<String>,
                              difficultyAdapter: ArrayAdapter<String>,
                              typeQuestionAdapter: ArrayAdapter<String>)

        fun saveTokenInSharedPreferrence(token: String, type: Int)

        fun showRetrieveDataError(error: String, title: String)

        fun successfullySave()
    }

    interface Presenter {
        fun RequestCategory(mView: android.view.View,
                            categoryArray: ArrayList<String>,
                            categoryIdArray: ArrayList<Long>,
                            difficultyArray: ArrayList<String>,
                            typeQuestionArray: ArrayList<String>)

        fun checkTokenExistInRealm(myRealm: Realm)

        fun saveInRealmDB(myRealm: Realm, token: String)

    }
}