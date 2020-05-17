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
import com.kilagbe.kilagbe.tools.RecycleViewAdapter


class PostGraduateBrowseFragment : Fragment(), RecycleViewAdapter.OnCatListener {

//  variables
    private lateinit var postgradMedicalRecyclerView: RecyclerView
    private lateinit var postgradEngineeringRecyclerView: RecyclerView
    private lateinit var postgradMbaRecyclerView: RecyclerView
    private lateinit var postgradMedicalAdapter: RecycleViewAdapter
    private lateinit var postgradEngineeringAdapter: RecycleViewAdapter
    private lateinit var postgradMbaAdapter: RecycleViewAdapter

    private var demoBookNames = arrayListOf<String>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_post_graduate_browse, container, false)


        postgradMedicalRecyclerView = root.findViewById(R.id.postgraduate_medical_recycler_view) as RecyclerView
        postgradEngineeringRecyclerView = root.findViewById(R.id.postgraduate_engineering_recycler_view) as RecyclerView
        postgradMbaRecyclerView = root.findViewById(R.id.postgraduate_mba_recycler_view) as RecyclerView

        /*getting demo book names data from string resources*/
        demoBookNames = resources.getStringArray(R.array.demo_book_names).toCollection(ArrayList())

        initRecyclerView()


        return root

    }


    private fun initRecyclerView(){

        postgradMedicalAdapter = RecycleViewAdapter(
            this.context,
            demoBookNames,
            this
        )
        postgradEngineeringAdapter =
            RecycleViewAdapter(
                this.context,
                demoBookNames,
                this
            )
        postgradMbaAdapter = RecycleViewAdapter(
            this.context,
            demoBookNames,
            this
        )

        postgradMedicalRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        postgradMedicalRecyclerView.adapter = postgradMedicalAdapter

        postgradEngineeringRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        postgradEngineeringRecyclerView.adapter = postgradEngineeringAdapter

        postgradMbaRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        postgradMbaRecyclerView.adapter = postgradMbaAdapter

    }



    override fun onCatClick(name: String?) {
        Toast.makeText(this.context, name, Toast.LENGTH_SHORT).show()
    }



}
