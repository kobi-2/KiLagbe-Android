package com.kilagbe.kilagbe.ui.categories

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
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


class EnglishMediumBrowseFragment : Fragment(), RecycleViewAdapter.OnCatListener, ItemOnClickListener.onExitListener, ItemHelper.getCategoryBookSuccessListener, ItemHelper.getCategoryBookFailureListener {


    private lateinit var englishMediumTopChartRecyclerView: RecyclerView
    private lateinit var navController : NavController

    lateinit var mContext: Context

    private lateinit var ih: ItemHelper

    private var demoBookNames = arrayListOf<String>()


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_english_medium_browse, container, false)


        englishMediumTopChartRecyclerView = root.findViewById(R.id.english_medium_top_chart_recycler_view) as RecyclerView

        /*getting demo book names data from string resources*/
        demoBookNames = resources.getStringArray(R.array.demo_book_names).toCollection(ArrayList())

        mContext = this.context!!

        ih = ItemHelper()
        ih.setGetCategoryBookSuccessListener(this)
        ih.setGetCategoryBookFailureListener(this)

        // FAB
        val fab: View = root.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            navController.navigate(R.id.navigation_cart)
        }

        return root
    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        initRecyclerView()
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }


    private fun initRecyclerView() {
        ih.getCategoryBook("EnglishMedium")
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
        val englishMediumTopChartAdapter = GroupAdapter<GroupieViewHolder>()
        bookArray.forEach {
            englishMediumTopChartAdapter.add(BookAdapter(it))
        }
        englishMediumTopChartRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL ,false)
        englishMediumTopChartRecyclerView.adapter = englishMediumTopChartAdapter
        val listener = ItemOnClickListener(mContext)
        listener.setOnExitListener(this)
        englishMediumTopChartAdapter.setOnItemClickListener(listener)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun getCategoryBookFailure() {
        Toast.makeText(mContext, "Failed to get books", Toast.LENGTH_SHORT).show()
    }
}
