package com.example.hexa_aaronlee.opentriviadb_demo.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.hexa_aaronlee.opentriviadb_demo.SharedPreference.MySharedPreference
import com.example.hexa_aaronlee.opentriviadb_demo.Presenter.MainPagePresenter
import com.example.hexa_aaronlee.opentriviadb_demo.R
import com.example.hexa_aaronlee.opentriviadb_demo.RealmObject.TokenInfoData
import com.example.hexa_aaronlee.opentriviadb_demo.View.MainPageView
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.fragment_main_page.*
import java.util.*
import io.realm.RealmResults


class MainPageFragment : Fragment(), MainPageView.View {

    lateinit var categoryArray: ArrayList<String>
    lateinit var categoryIdArray: ArrayList<Long>
    lateinit var difficultyArray: ArrayList<String>
    lateinit var typeQuestionArray: ArrayList<String>
    lateinit var typeQuestionIdArray: ArrayList<String>
    lateinit var mySharedPreferences: MySharedPreference
    lateinit var myPresenter: MainPagePresenter
    lateinit var myRealm: Realm

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Realm.init(view.context)
        val config = RealmConfiguration.Builder().name("token.realm").build()
        myRealm = Realm.getInstance(config)

        mySharedPreferences = MySharedPreference(view.context)

        myPresenter = MainPagePresenter(this)

        categoryArray = ArrayList()
        categoryIdArray = ArrayList()

        categoryArray.add("Default")
        categoryIdArray.add(0)

        difficultyArray = arrayListOf("Default", "Easy", "Medium", "Hard")
        typeQuestionArray = arrayListOf("Default", "True / False", "Multiple Choice")
        typeQuestionIdArray = arrayListOf("default", "boolean", "multiple")

        myPresenter.checkTokenExistInRealm(myRealm)

        myPresenter.RequestCategory(view, categoryArray, categoryIdArray, difficultyArray, typeQuestionArray)



        nextBtn.setOnClickListener {

            getDataFromSpinner()

            it.findNavController().navigate(R.id.nav_mainPagrFragment_to_questionPageFragment)
        }

        viewCountBtn.setOnClickListener {
            it.findNavController().navigate(R.id.nav_mainPagrFragment_to_viewQuestionPageFragment)
        }
    }

    override fun hideLoadingIndicator() {
        loadingIndicator.visibility = View.INVISIBLE
    }

    override fun setAdapterSpinner(categoryAdapter: ArrayAdapter<String>, difficultyAdapter: ArrayAdapter<String>, typeQuestionAdapter: ArrayAdapter<String>) {
        spinnerCategory.adapter = categoryAdapter

        spinnerDifficulty.adapter = difficultyAdapter

        spinnerType.adapter = typeQuestionAdapter
    }


    fun getDataFromSpinner() {

        val difficulty = spinnerDifficulty.selectedItem.toString()

        val categoryIdPosition = spinnerCategory.selectedItemPosition
        val typeIdPosition = spinnerType.selectedItemPosition

        val categoryId = categoryIdArray[categoryIdPosition]
        val typeQuestion = typeQuestionIdArray[typeIdPosition]

        mySharedPreferences.setCategory(categoryId.toString())
        mySharedPreferences.setDifficulty(difficulty.toLowerCase())
        mySharedPreferences.setType(typeQuestion)

    }

    override fun saveTokenInSharedPreferrence(token: String, type: Int) {

        if (type == 0) {
            mySharedPreferences.setToken(token)
        } else if (type == 1) {
            mySharedPreferences.setToken(token)

            myPresenter.saveInRealmDB(myRealm, token)
        }
    }

    override fun showRetrieveDataError(error: String, title: String) {
        Log.e(title, error)
    }

    override fun successfullySave() {
        Log.i("Token Add", "Successfully")
    }
}

