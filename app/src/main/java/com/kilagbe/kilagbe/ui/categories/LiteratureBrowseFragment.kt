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


class LiteratureBrowseFragment : Fragment(), RecycleViewAdapter.OnCatListener, ItemOnClickListener.onExitListener, ItemHelper.getCategoryBookFailureListener, ItemHelper.getCategoryBookSuccessListener {


    private lateinit var literatureTopChatRecyclerView: RecyclerView

    lateinit var mContext: Context

    private lateinit var ih: ItemHelper

    private var demoBookNames = arrayListOf<String>()

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //TODO: change the layout
        val root = inflater.inflate(R.layout.fragment_literature_browse, container, false)

        //todo: change topchart
        literatureTopChatRecyclerView = root.findViewById(R.id.literature_top_chart_recycler_view) as RecyclerView

        /*getting demo book names data from string resources*/
        demoBookNames = resources.getStringArray(R.array.demo_book_names).toCollection(ArrayList())

        mContext = this.context!!

        ih = ItemHelper()
        ih.setGetCategoryBookSuccessListener(this)
        ih.setGetCategoryBookFailureListener(this)
        return root

    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        initRecyclerView()
        super.onStart()
    }

    private fun initRecyclerView() {
        ih.getCategoryBook("Literature")
    }

    override fun onCatClick(name: String?) {
        Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onExit() {
        initRecyclerView()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun getCategoryBookSuccess(bookArray: ArrayList<Book>) {
        val literatureTopChartAdapter = GroupAdapter<GroupieViewHolder>()
        bookArray.forEach {
            literatureTopChartAdapter.add(BookAdapter(it))
        }
        literatureTopChatRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL ,false)
        literatureTopChatRecyclerView.adapter = literatureTopChartAdapter
        val listener = ItemOnClickListener(mContext)
        listener.setOnExitListener(this)
        literatureTopChartAdapter.setOnItemClickListener(listener)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun getCategoryBookFailure() {
        Toast.makeText(mContext, "Failed to get books", Toast.LENGTH_SHORT).show()
    }
}
