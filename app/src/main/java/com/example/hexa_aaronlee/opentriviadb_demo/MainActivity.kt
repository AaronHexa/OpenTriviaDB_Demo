package com.example.hexa_aaronlee.opentriviadb_demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var actionBar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar = findViewById<Toolbar>(R.id.toolbar)

        val navController = findNavController(R.id.my_nav_host)

        setSupportActionBar(actionBar)

        NavigationUI.setupActionBarWithNavController(this,navController)

        checkFragmentPage()
    }

    fun checkFragmentPage(){
        val currentFragment = findNavController(R.id.my_nav_host).currentDestination!!.id

        when (currentFragment) {
            R.id.mainPagrFragment -> supportActionBar!!.title = resources.getString(R.string.app_name)
            R.id.questionPageFragment -> {
                supportActionBar!!.title = "Question"
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }
            R.id.viewQuestionPageFragment -> {
                supportActionBar!!.title = "Question Category"
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.my_nav_host).navigateUp()

    override fun onBackPressed() {
        val currentFragment = findNavController(R.id.my_nav_host).currentDestination!!.id

        when (currentFragment) {
            R.id.mainPagrFragment -> finish()
        }
    }
}
