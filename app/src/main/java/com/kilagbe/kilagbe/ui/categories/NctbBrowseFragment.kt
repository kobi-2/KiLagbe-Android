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


class NctbBrowseFragment : Fragment(), RecycleViewAdapter.OnCatListener, ItemOnClickListener.onExitListener {


    private lateinit var nctbTopChatRecyclerView: RecyclerView
//    private lateinit var nctbTopChartAdapter: RecycleViewAdapter

    private var demoBookNames = arrayListOf<String>()


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_nctb_browse, container, false)


        nctbTopChatRecyclerView = root.findViewById(R.id.nctb_top_chart_recycler_view) as RecyclerView

        /*getting demo book names data from string resources*/
        demoBookNames = resources.getStringArray(R.array.demo_book_names).toCollection(ArrayList())

        return root

    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        initRecyclerView(this.activity!!)
        super.onStart()
    }


    private fun initRecyclerView(context : Context){

        val nctbTopChartAdapter = GroupAdapter<GroupieViewHolder>()

        FirebaseFirestore.getInstance().collection("books").get()
            .addOnSuccessListener {
                for ( doc in it!! )
                {
                    val temp = doc.toObject(Book::class.java)
                    nctbTopChartAdapter.add(BookAdapter(temp))
                }
                nctbTopChatRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
                nctbTopChatRecyclerView.adapter = nctbTopChartAdapter
                val listener = ItemOnClickListener(context)
                listener.setOnExitListener(this)
                nctbTopChartAdapter.setOnItemClickListener(listener)
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
        initRecyclerView(this.activity!!)
    }


}
