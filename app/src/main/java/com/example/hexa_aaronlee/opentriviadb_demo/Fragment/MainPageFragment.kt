package com.example.hexa_aaronlee.opentriviadb_demo.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.hexa_aaronlee.opentriviadb_demo.MainActivity
import com.example.hexa_aaronlee.opentriviadb_demo.SharedPreference.MySharedPreference
import com.example.hexa_aaronlee.opentriviadb_demo.Presenter.MainPagePresenter
import com.example.hexa_aaronlee.opentriviadb_demo.R
import com.example.hexa_aaronlee.opentriviadb_demo.View.MainPageView
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.fragment_main_page.*
import java.util.*


class MainPageFragment : Fragment(), MainPageView.View {

    lateinit var categoryArray: ArrayList<String>
    lateinit var categoryIdArray: ArrayList<Long>
    lateinit var difficultyArray: ArrayList<String>
    lateinit var typeQuestionArray: ArrayList<String>
    lateinit var typeQuestionIdArray: ArrayList<String>
    lateinit var mySharedPreferences: MySharedPreference
    lateinit var myPresenter: MainPagePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.title = resources.getString(R.string.app_name)

        mySharedPreferences = MySharedPreference(view.context)

        myPresenter = MainPagePresenter(this)

        categoryArray = ArrayList()
        categoryIdArray = ArrayList()

        categoryArray.add("Default")
        categoryIdArray.add(0)

        difficultyArray = arrayListOf("Default", "Easy", "Medium", "Hard")
        typeQuestionArray = arrayListOf("Default", "True / False", "Multiple Choice")
        typeQuestionIdArray = arrayListOf("default", "boolean", "multiple")

        myPresenter.checkTokenExistInSharePreference(mySharedPreferences)

        myPresenter.requestCategory(view, categoryArray, categoryIdArray, difficultyArray, typeQuestionArray)

        nextBtn.isClickable = false

        viewCountBtn.isClickable = false
    }

    override fun hideLoadingIndicator() {
        MainProgressBar.visibility = View.INVISIBLE
    }

    override fun setAdapterSpinner(categoryArray: ArrayList<String>,
                                   difficultyArray: ArrayList<String>,
                                   typeQuestionArray: ArrayList<String>) {

        val categoryAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, categoryArray)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val difficultyAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, difficultyArray)
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        val typeQuestionAdapter = ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item, typeQuestionArray)
        typeQuestionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCategory.adapter = categoryAdapter

        spinnerDifficulty.adapter = difficultyAdapter

        spinnerType.adapter = typeQuestionAdapter

        nextBtn.isClickable = true

        viewCountBtn.isClickable = true

        nextBtn.setOnClickListener {

            getDataFromSpinner()

            it.findNavController().navigate(R.id.nav_mainPagrFragment_to_questionPageFragment)
        }

        viewCountBtn.setOnClickListener {
            it.findNavController().navigate(R.id.nav_mainPagrFragment_to_viewQuestionPageFragment)
        }
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

    override fun saveTokenInSharedPreferrence(token: String) {
        mySharedPreferences.setToken(token)
    }

    override fun showRetrieveDataError(error: String, title: String) {
        Log.e(title, error)
    }

    override fun successfullySave() {
        Log.i("Token Add", "Successfully")
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

