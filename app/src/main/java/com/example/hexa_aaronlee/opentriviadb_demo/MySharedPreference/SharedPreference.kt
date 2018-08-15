package com.example.hexa_aaronlee.opentriviadb_demo.MySharedPreference

import android.content.Context

class SharedPreference(val mContext : Context) {

    val PREFERENCE_NAME = "SharePreference"

    val PREFERENCE_Category = "Category"
    val PREFERENCE_Difficulty = "Difficulty"
    val PREFERENCE_Type = "Type"
    val PREFERENCE_Token = "Token"

    val preference = mContext.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)

    //... Selection about Category
    fun getCategory() : String{
        return preference.getString(PREFERENCE_Category,"")
    }

    fun setCategory(category: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_Category,category)
        editor.apply()
    }

    //... Selection about difficulty
    fun getDifficulty() : String{
        return preference.getString(PREFERENCE_Difficulty,"")
    }

    fun setDifficulty(difficulty: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_Difficulty,difficulty)
        editor.apply()
    }

    //... Selection about Type of Question
    fun getType() : String{
        return preference.getString(PREFERENCE_Type,"")
    }

    fun setType(type: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_Type,type)
        editor.apply()
    }

    //... Token Api
    fun getToken() : String{
        return preference.getString(PREFERENCE_Token,"")
    }

    fun setToken(token: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_Type,token)
        editor.apply()
    }
}