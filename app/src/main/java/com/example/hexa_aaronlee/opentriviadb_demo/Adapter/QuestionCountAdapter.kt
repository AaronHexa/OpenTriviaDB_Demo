package com.example.hexa_aaronlee.opentriviadb_demo.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.hexa_aaronlee.opentriviadb_demo.R


class QuestionCountAdapter(private val mContext : Context,
                           val mTotalCount : ArrayList<Int>,
                           val mCategoryList : ArrayList<String>,
                           val mEasyList : ArrayList<Int>,
                           val mMediumList : ArrayList<Int>,
                           val mHardList : ArrayList<Int>) : RecyclerView.Adapter<QuestionCountAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_question_count_box,parent,false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return mCategoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.categoryTxt.text = mCategoryList[position]
        holder.totalCountTxt.text = mTotalCount[position].toString()
        holder.easyCountTxt.text = "Easy : ${mEasyList[position]}"
        holder.mediumCountTxt.text = "Easy : ${mMediumList[position]}"
        holder.hardCountTxt.text = "Easy : ${mHardList[position]}"

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val categoryTxt = itemView.findViewById<TextView>(R.id.questionCountCategory)
        val totalCountTxt = itemView.findViewById<TextView>(R.id.countTotal)
        val easyCountTxt = itemView.findViewById<TextView>(R.id.easyTotal)
        val mediumCountTxt = itemView.findViewById<TextView>(R.id.mediumTotal)
        val hardCountTxt = itemView.findViewById<TextView>(R.id.hardTotal)
    }
}