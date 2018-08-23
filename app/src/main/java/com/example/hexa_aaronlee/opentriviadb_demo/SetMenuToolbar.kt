package com.example.hexa_aaronlee.opentriviadb_demo

import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView

class SetMenuToolbar(activity: MainActivity) {

    val view = activity
    val iconToolbar = view.findViewById<ImageView>(R.id.iconToolbar)
    val titleToolbar = view.findViewById<TextView>(R.id.toolbarTitle)

    fun setToolbarTitle(title: String){
        titleToolbar.text = title
    }

    fun setToolbarBackIcon(title: String){

        titleToolbar.text = title

        iconToolbar.setImageResource(R.drawable.ic_arrow_back_black_24dp)
    }

    fun setEmptyIcon(title: String){

        titleToolbar.text = title

        iconToolbar.setImageResource(R.color.colorTrans)
    }
}