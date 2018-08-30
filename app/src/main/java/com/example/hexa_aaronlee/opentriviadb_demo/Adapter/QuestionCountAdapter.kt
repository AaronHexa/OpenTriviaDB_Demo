package com.example.hexa_aaronlee.opentriviadb_demo.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.CategoryData
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.QuestionCountArray
import com.example.hexa_aaronlee.opentriviadb_demo.R


class QuestionCountAdapter(private val mContext: Context,
                           val mMutableMap: MutableMap<String,QuestionCountArray>,
                           val mCategory : ArrayList<CategoryData>) : RecyclerView.Adapter<QuestionCountAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_question_count_box, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return mCategory[0].triviaCategories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        for (i in 0 until  mCategory[0].triviaCategories.size){
            if (mMutableMap["$position"]?.mCategoryId == mCategory[0].triviaCategories[i].id.toString()){
                holder.categoryTxt.text = mCategory[0].triviaCategories[i].name
            }
        }
        holder.totalCountTxt.text = mMutableMap["$position"]?.mCount.toString()
        holder.easyCountTxt.text = "Easy : ${mMutableMap["$position"]?.mEasyCount}"
        holder.mediumCountTxt.text = "Medium : ${mMutableMap["$position"]?.mMediumCount}"
        holder.hardCountTxt.text = "Hard : ${mMutableMap["$position"]?.mHardCount}"

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryTxt = itemView.findViewById<TextView>(R.id.questionCountCategory) ?: throw IllegalAccessException("TextView is null")
        val totalCountTxt = itemView.findViewById<TextView>(R.id.countTotal) ?: throw IllegalAccessException("TextView is null")
        val easyCountTxt = itemView.findViewById<TextView>(R.id.easyTotal) ?: throw IllegalAccessException("TextView is null")
        val mediumCountTxt = itemView.findViewById<TextView>(R.id.mediumTotal) ?: throw IllegalAccessException("TextView is null")
        val hardCountTxt = itemView.findViewById<TextView>(R.id.hardTotal) ?: throw IllegalAccessException("TextView is null")
    }
}