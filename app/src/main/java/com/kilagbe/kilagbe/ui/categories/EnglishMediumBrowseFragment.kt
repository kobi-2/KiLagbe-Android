package com.kilagbe.kilagbe.ui.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.RecycleViewAdapter


class EnglishMediumBrowseFragment : Fragment(), RecycleViewAdapter.OnCatListener {

    //  variables
    private lateinit var englishMediumTopChartRecyclerView: RecyclerView
    private lateinit var englishMediumTopChartAdapter: RecycleViewAdapter

    private var demoBookNames = arrayListOf<String>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_english_medium_browse, container, false)


        englishMediumTopChartRecyclerView = root.findViewById(R.id.english_medium_top_chart_recycler_view) as RecyclerView

        /*getting demo book names data from string resources*/
        demoBookNames = resources.getStringArray(R.array.demo_book_names).toCollection(ArrayList())


        initRecyclerView()

        return root

    }




    private fun initRecyclerView(){

        englishMediumTopChartAdapter = RecycleViewAdapter(this.context, demoBookNames, this)

        englishMediumTopChartRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        englishMediumTopChartRecyclerView.adapter = englishMediumTopChartAdapter


    }



    override fun onCatClick(name: String?) {
        Toast.makeText(this.context, name, Toast.LENGTH_SHORT).show()
    }



}
