package com.example.hexa_aaronlee.opentriviadb_demo.ObjectData

object QuestionCountArray {

    var mCountArray: ArrayList<Int> = ArrayList()
    var mEasyCountArray: ArrayList<Int> = ArrayList()
    var mMediumCountArray: ArrayList<Int> = ArrayList()
    var mHardCountArray: ArrayList<Int> = ArrayList()
    var mCategoryArray: ArrayList<String> = ArrayList()
    var mCategoryIdArray: ArrayList<String> = ArrayList()
    var mCompareId :ArrayList<String> = ArrayList()

    fun resetArray(){
        mCountArray = ArrayList()
        mEasyCountArray = ArrayList()
        mMediumCountArray = ArrayList()
        mHardCountArray = ArrayList()
        mCategoryArray = ArrayList()
        mCategoryIdArray = ArrayList()
        mCompareId = ArrayList()
    }

}