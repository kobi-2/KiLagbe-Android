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
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Book
import com.kilagbe.kilagbe.databasing.ItemHelper
import com.kilagbe.kilagbe.tools.BookAdapter
import com.kilagbe.kilagbe.tools.ItemOnClickListener
import com.kilagbe.kilagbe.tools.RecycleViewAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class UndergraduateBrowseFragment : Fragment(), RecycleViewAdapter.OnCatListener, ItemOnClickListener.onExitListener, ItemHelper.getDoubleCategoryBookSuccessListener, ItemHelper.getDoubleCategoryBookFailureListener {


    private lateinit var undergradMedicalRecyclerView: RecyclerView
    private lateinit var undergradEngineeringRecyclerView: RecyclerView
    private lateinit var undergradBbaRecyclerView: RecyclerView

    lateinit var mContext: Context
    lateinit var ih: ItemHelper

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

        mContext = this.context!!

        ih = ItemHelper()
        ih.setGetDoubleCategoryBookSuccessListener(this)
        ih.setGetDoubleCategoryBookFailureListener(this)

        return root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        initRecyclerView()
        super.onStart()
    }


    private fun initRecyclerView() {

        ih.getDoubleCategoryBook("Undergraduate", "Medical")
        ih.getDoubleCategoryBook("Undergraduate", "Engineering")
        ih.getDoubleCategoryBook("Undergraduate", "BBA")

    }



    override fun onCatClick(name: String?) {
        Toast.makeText(this.context, name, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onExit() {
        initRecyclerView()
    }

    override fun getDoubleCategoryBookSuccess(bookArray: ArrayList<Book>, cat2: String) {
        val adapter = GroupAdapter<GroupieViewHolder>()
        lateinit var recycler: RecyclerView
        when (cat2) {
            "Medical" -> {
                recycler = undergradMedicalRecyclerView
            }
            "Engineering" -> {
                recycler = undergradEngineeringRecyclerView
            }
            "BBA" -> {
                recycler = undergradBbaRecyclerView
            }
        }
        bookArray.forEach {
            adapter.add(BookAdapter(it))
        }
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL ,false)
        val listener = ItemOnClickListener(mContext)
        listener.setOnExitListener(this)
        adapter.setOnItemClickListener(listener)
    }

    override fun getDoubleCategoryBookFailure() {
        Toast.makeText(mContext, "Failed to get books", Toast.LENGTH_SHORT).show()
    }
}
