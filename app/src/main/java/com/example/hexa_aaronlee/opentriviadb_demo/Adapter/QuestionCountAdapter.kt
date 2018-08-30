package com.example.hexa_aaronlee.opentriviadb_demo.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.QuestionCountArray
import com.example.hexa_aaronlee.opentriviadb_demo.R


class QuestionCountAdapter(private val mContext: Context) : RecyclerView.Adapter<QuestionCountAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_question_count_box, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return QuestionCountArray.mCategoryArray.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        for (i in QuestionCountArray.mCompareId.indices){
            if (QuestionCountArray.mCompareId[position] == QuestionCountArray.mCategoryIdArray[i]){
                holder.categoryTxt.text = QuestionCountArray.mCategoryArray[i]
            }
        }
        holder.totalCountTxt.text = QuestionCountArray.mCountArray[position].toString()
        holder.easyCountTxt.text = "Easy : ${QuestionCountArray.mEasyCountArray[position]}"
        holder.mediumCountTxt.text = "Medium : ${QuestionCountArray.mMediumCountArray[position]}"
        holder.hardCountTxt.text = "Hard : ${QuestionCountArray.mHardCountArray[position]}"

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryTxt = itemView.findViewById<TextView>(R.id.questionCountCategory) ?: throw IllegalAccessException("TextView is null")
        val totalCountTxt = itemView.findViewById<TextView>(R.id.countTotal) ?: throw IllegalAccessException("TextView is null")
        val easyCountTxt = itemView.findViewById<TextView>(R.id.easyTotal) ?: throw IllegalAccessException("TextView is null")
        val mediumCountTxt = itemView.findViewById<TextView>(R.id.mediumTotal) ?: throw IllegalAccessException("TextView is null")
        val hardCountTxt = itemView.findViewById<TextView>(R.id.hardTotal) ?: throw IllegalAccessException("TextView is null")
    }
}