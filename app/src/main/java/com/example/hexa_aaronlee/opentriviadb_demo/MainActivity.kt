package com.example.hexa_aaronlee.opentriviadb_demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var setMenuToolbar: SetMenuToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setMenuToolbar = SetMenuToolbar(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

        setMenuToolbar.setToolbarTitle(resources.getString(R.string.app_name))

        iconToolbar.setOnClickListener {
            checkFragmentPage()
        }
    }

    fun checkFragmentPage(){
        val currentFragment = findNavController(R.id.my_nav_host).currentDestination!!.id

        when (currentFragment) {
            R.id.mainPagrFragment -> Log.i("exits", " Yes")

            else -> onSupportNavigateUp()
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.my_nav_host).navigateUp()

    override fun onBackPressed() {
        checkFragmentPage()
    }
}
