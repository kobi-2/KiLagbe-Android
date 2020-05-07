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


class NctbBrowseFragment : Fragment(), RecycleViewAdapter.OnCatListener {


    //  variables
    private lateinit var nctbTopChatRecyclerView: RecyclerView
    private lateinit var nctbTopChartAdapter: RecycleViewAdapter

    private var demoBookNames = arrayListOf<String>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_nctb_browse, container, false)


        nctbTopChatRecyclerView = root.findViewById(R.id.nctb_top_chart_recycler_view) as RecyclerView

        /*getting demo book names data from string resources*/
        demoBookNames = resources.getStringArray(R.array.demo_book_names).toCollection(ArrayList())


        initRecyclerView()

        return root

    }



    private fun initRecyclerView(){

        nctbTopChartAdapter = RecycleViewAdapter(this.context, demoBookNames, this)

        nctbTopChatRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        nctbTopChatRecyclerView.adapter = nctbTopChartAdapter


    }



    override fun onCatClick(name: String?) {
        Toast.makeText(this.context, name, Toast.LENGTH_SHORT).show()
    }



}
