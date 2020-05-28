package com.kilagbe.kilagbe.ui.categories

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore

import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Book
import com.kilagbe.kilagbe.tools.BookAdapter
import com.kilagbe.kilagbe.tools.ItemOnClickListener
import com.kilagbe.kilagbe.tools.RecycleViewAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class UndergraduateBrowseFragment : Fragment(), RecycleViewAdapter.OnCatListener, ItemOnClickListener.onExitListener {


    private lateinit var undergradMedicalRecyclerView: RecyclerView
    private lateinit var undergradEngineeringRecyclerView: RecyclerView
    private lateinit var undergradBbaRecyclerView: RecyclerView
//    private lateinit var undergradMedicalAdapter: RecycleViewAdapter
//    private lateinit var undergradEngineeringAdapter: RecycleViewAdapter
//    private lateinit var undergradBbaAdapter: RecycleViewAdapter

    private var demoBookNames = arrayListOf<String>()



    @SuppressLint("UseRequireInsteadOfGet")
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



        return root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        initRecyclerView(this!!.activity!!)
        super.onStart()
    }


    private fun initRecyclerView(context: Context){

        val undergradMedicalAdapter = GroupAdapter<GroupieViewHolder>()
        val undergradEngineeringAdapter = GroupAdapter<GroupieViewHolder>()
        val undergradBbaAdapter = GroupAdapter<GroupieViewHolder>()

        FirebaseFirestore.getInstance().collection("books").get()
            .addOnSuccessListener {
                for ( doc in it!! )
                {
                    val temp = doc.toObject(Book::class.java)
                    undergradMedicalAdapter.add(BookAdapter(temp))
                }
                undergradMedicalRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
                undergradMedicalRecyclerView.adapter = undergradMedicalAdapter
                val listener = ItemOnClickListener(context)
                undergradMedicalAdapter.setOnItemClickListener(listener)
            }
            .addOnFailureListener {
                Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
            }

        FirebaseFirestore.getInstance().collection("books").get()
            .addOnSuccessListener {
                for ( doc in it!! )
                {
                    val temp = doc.toObject(Book::class.java)
                    undergradEngineeringAdapter.add(BookAdapter(temp))
                }
                undergradEngineeringRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
                undergradEngineeringRecyclerView.adapter = undergradEngineeringAdapter
                val listener = ItemOnClickListener(context)
                undergradEngineeringAdapter.setOnItemClickListener(listener)
            }
            .addOnFailureListener {
                Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
            }

        FirebaseFirestore.getInstance().collection("books").get()
            .addOnSuccessListener {
                for ( doc in it!! )
                {
                    val temp = doc.toObject(Book::class.java)
                    undergradBbaAdapter.add(BookAdapter(temp))
                }
                undergradBbaRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
                undergradBbaRecyclerView.adapter = undergradBbaAdapter
                val listener = ItemOnClickListener(context)
                listener.setOnExitListener(this)
                undergradBbaAdapter.setOnItemClickListener(listener)
            }
            .addOnFailureListener {
                Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
            }



    }



    override fun onCatClick(name: String?) {
        Toast.makeText(this.context, name, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onExit() {
        initRecyclerView(this!!.activity!!)
    }


}
