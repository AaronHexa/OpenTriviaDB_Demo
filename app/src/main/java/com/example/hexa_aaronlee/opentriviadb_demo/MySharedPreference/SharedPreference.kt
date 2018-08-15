package com.example.hexa_aaronlee.opentriviadb_demo.MySharedPreference

import android.content.Context

class SharedPreference(val mContext : Context) {

    val PREFERENCE_NAME = "SharePreference"

    val PREFERENCE_Category = "Category"
    val PREFERENCE_Difficulty = "Difficulty"
    val PREFERENCE_Type = "Type"

    val preference = mContext.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)

    fun getCategory() : String{
        return preference.getString(PREFERENCE_Category,"")
    }

    fun setCategory(category: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_Category,category)
        editor.apply()
    }

    fun getDifficulty() : String{
        return preference.getString(PREFERENCE_Category,"")
    }

    fun setDifficulty(category: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_Category,category)
        editor.apply()
    }

    fun getType() : String{
        return preference.getString(PREFERENCE_Category,"")
    }

    fun setType(category: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_Category,category)
        editor.apply()
    }
}