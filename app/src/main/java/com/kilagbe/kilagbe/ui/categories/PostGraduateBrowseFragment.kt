package com.kilagbe.kilagbe.ui.categories

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Book
import com.kilagbe.kilagbe.tools.BookAdapter
import com.kilagbe.kilagbe.tools.ItemOnClickListener
import com.kilagbe.kilagbe.tools.RecycleViewAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


class PostGraduateBrowseFragment : Fragment(), RecycleViewAdapter.OnCatListener, ItemOnClickListener.onExitListener {


    private lateinit var postgradMedicalRecyclerView: RecyclerView
    private lateinit var postgradEngineeringRecyclerView: RecyclerView
    private lateinit var postgradMbaRecyclerView: RecyclerView
//    private lateinit var postgradMedicalAdapter: RecycleViewAdapter
//    private lateinit var postgradEngineeringAdapter: RecycleViewAdapter
//    private lateinit var postgradMbaAdapter: RecycleViewAdapter

    private var demoBookNames = arrayListOf<String>()



    @SuppressLint("UseRequireInsteadOfGet")
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


        return root

    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        initRecyclerView(this!!.activity!!)
        super.onStart()
    }

    private fun initRecyclerView(context: Context){

        val postgradMedicalAdapter = GroupAdapter<GroupieViewHolder>()
        val postgradEngineeringAdapter = GroupAdapter<GroupieViewHolder>()
        val postgradMbaAdapter = GroupAdapter<GroupieViewHolder>()

        FirebaseFirestore.getInstance().collection("books").get()
            .addOnSuccessListener {
                for ( doc in it!! )
                {
                    val temp = doc.toObject(Book::class.java)
                    postgradMedicalAdapter.add(BookAdapter(temp))
                }
                postgradMedicalRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
                postgradMedicalRecyclerView.adapter = postgradMedicalAdapter
                val listener = ItemOnClickListener(context)
                postgradMedicalAdapter.setOnItemClickListener(listener)
            }
            .addOnFailureListener {
                Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
            }

        FirebaseFirestore.getInstance().collection("books").get()
            .addOnSuccessListener {
                for ( doc in it!! )
                {
                    val temp = doc.toObject(Book::class.java)
                    postgradEngineeringAdapter.add(BookAdapter(temp))
                }
                postgradEngineeringRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
                postgradEngineeringRecyclerView.adapter = postgradEngineeringAdapter
                val listener = ItemOnClickListener(context)
                postgradEngineeringAdapter.setOnItemClickListener(listener)
            }
            .addOnFailureListener {
                Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
            }

        FirebaseFirestore.getInstance().collection("books").get()
            .addOnSuccessListener {
                for ( doc in it!! )
                {
                    val temp = doc.toObject(Book::class.java)
                    postgradMbaAdapter.add(BookAdapter(temp))
                }
                postgradMbaRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
                postgradMbaRecyclerView.adapter = postgradMbaAdapter
                val listener = ItemOnClickListener(context)
                listener.setOnExitListener(this)
                postgradMbaAdapter.setOnItemClickListener(listener)
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
