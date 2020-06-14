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


class PostGraduateBrowseFragment : Fragment(), RecycleViewAdapter.OnCatListener, ItemOnClickListener.onExitListener, ItemHelper.getDoubleCategoryBookSuccessListener, ItemHelper.getDoubleCategoryBookFailureListener {


    lateinit var mContext: Context
    lateinit var ih: ItemHelper

    private lateinit var postgradMedicalRecyclerView: RecyclerView
    private lateinit var postgradEngineeringRecyclerView: RecyclerView
    private lateinit var postgradMbaRecyclerView: RecyclerView


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

        ih.getDoubleCategoryBook("Postgraduate", "Medical")
        ih.getDoubleCategoryBook("Postgraduate", "Engineering")
        ih.getDoubleCategoryBook("Postgraduate", "MBA")
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
                recycler = postgradMedicalRecyclerView
            }
            "Engineering" -> {
                recycler = postgradEngineeringRecyclerView
            }
            "MBA" -> {
                recycler = postgradMbaRecyclerView
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
