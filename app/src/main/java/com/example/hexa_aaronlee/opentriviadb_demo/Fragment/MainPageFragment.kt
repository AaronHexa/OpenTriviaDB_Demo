package com.example.hexa_aaronlee.opentriviadb_demo.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.hexa_aaronlee.opentriviadb_demo.R
import kotlinx.android.synthetic.main.fragment_main_page.*


class MainPageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nextBtn.setOnClickListener{
            it.findNavController().navigate(R.id.nav_mainPagrFragment_to_questionPageFragment)
        }

        viewCountBtn.setOnClickListener {
            it.findNavController().navigate(R.id.nav_mainPagrFragment_to_viewQuestionPageFragment)
        }
    }

}
