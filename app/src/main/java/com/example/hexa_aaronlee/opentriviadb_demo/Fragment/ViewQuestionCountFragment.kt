package com.example.hexa_aaronlee.opentriviadb_demo.Fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hexa_aaronlee.opentriviadb_demo.Adapter.QuestionCountAdapter
import com.example.hexa_aaronlee.opentriviadb_demo.MainActivity
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.CategoryData
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.QuestionCountArray
import com.example.hexa_aaronlee.opentriviadb_demo.Presenter.ViewQuestionCountPresenter
import com.example.hexa_aaronlee.opentriviadb_demo.R
import com.example.hexa_aaronlee.opentriviadb_demo.View.ViewQuestionCountView
import kotlinx.android.synthetic.main.fragment_view_question_page.*
import kotlin.collections.ArrayList


class ViewQuestionCountFragment : Fragment(), ViewQuestionCountView.View {

    lateinit var myPresenter: ViewQuestionCountPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_question_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.title = "Question Category"
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewQuestionBar.visibility = View.VISIBLE

        myPresenter = ViewQuestionCountPresenter(this)

        myPresenter.setRetrofitAndMapper(view)

        listViewCount.setHasFixedSize(true)
        listViewCount.layoutManager = LinearLayoutManager(view.context)

        myPresenter.RequestGetCategory()
    }

    override fun setListView(mMutableQuestionCount : MutableMap<String,QuestionCountArray>,
                             mCategory : ArrayList<CategoryData>) {

        ViewQuestionBar.visibility = View.INVISIBLE

        val context : Context = view?.context ?: throw IllegalAccessException("Context is Null")

        val myAdapter = QuestionCountAdapter(context, mMutableQuestionCount,mCategory)
        listViewCount.adapter = myAdapter
    }

    override fun showErrorGetData(e: String) {
        Log.e("Get Data Error", e)
    }

    override fun onDestroy() {
        myPresenter.onDestroy()
        super.onDestroy()
    }
}
