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


class UndergraduateBrowseFragment : Fragment(), RecycleViewAdapter.OnCatListener {

//  variables
    private lateinit var undergradMedicalRecyclerView: RecyclerView
    private lateinit var undergradEngineeringRecyclerView: RecyclerView
    private lateinit var undergradBbaRecyclerView: RecyclerView
    private lateinit var undergradMedicalAdapter: RecycleViewAdapter
    private lateinit var undergradEngineeringAdapter: RecycleViewAdapter
    private lateinit var undergradBbaAdapter: RecycleViewAdapter

    private var demoBookNames = arrayListOf<String>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_undergraduate_browse, container, false)


        undergradMedicalRecyclerView = root.findViewById(R.id.undergraduate_medical_recycler_view) as RecyclerView
        undergradEngineeringRecyclerView = root.findViewById(R.id.undergraduate_engineering_recycler_view) as RecyclerView
        undergradBbaRecyclerView = root.findViewById(R.id.undergraduate_bba_recycler_view) as RecyclerView

        /*getting demo book names data from string resources*/
        demoBookNames = resources.getStringArray(R.array.demo_book_names).toCollection(ArrayList())

        initRecyclerView()


        return root
    }




    private fun initRecyclerView(){

        undergradMedicalAdapter = RecycleViewAdapter(
            this.context,
            demoBookNames,
            this
        )
        undergradEngineeringAdapter =
            RecycleViewAdapter(
                this.context,
                demoBookNames,
                this
            )
        undergradBbaAdapter = RecycleViewAdapter(
            this.context,
            demoBookNames,
            this
        )

        undergradMedicalRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        undergradMedicalRecyclerView.adapter = undergradMedicalAdapter

        undergradEngineeringRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        undergradEngineeringRecyclerView.adapter = undergradEngineeringAdapter

        undergradBbaRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        undergradBbaRecyclerView.adapter = undergradBbaAdapter

    }



    override fun onCatClick(name: String?) {
        Toast.makeText(this.context, name, Toast.LENGTH_SHORT).show()
    }


}
