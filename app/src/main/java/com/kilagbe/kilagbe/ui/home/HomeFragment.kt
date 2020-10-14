package com.kilagbe.kilagbe.ui.home

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
import com.kilagbe.kilagbe.data.Essential
import com.kilagbe.kilagbe.databasing.ItemHelper
import com.kilagbe.kilagbe.tools.BookAdapter
import com.kilagbe.kilagbe.tools.EssentialAdapter
import com.kilagbe.kilagbe.tools.ItemOnClickListener
import com.kilagbe.kilagbe.tools.RecycleViewAdapter
import com.kilagbe.kilagbe.tools.RecycleViewAdapter.OnCatListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


class HomeFragment : Fragment(), OnCatListener, ItemOnClickListener.onExitListener, ItemHelper.getAllEssentialsSuccessListener, ItemHelper.getAllEssentialsFailureListener, ItemHelper.getAllBooksSuccessListener, ItemHelper.getAllBooksFailureListener{

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onExit() {
        initRecyclerView(this.activity!!)
    }

    private lateinit var navController : NavController

    lateinit var mContext: Context

    private lateinit var ih: ItemHelper

    private lateinit var essentialRecyclerView: RecyclerView
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var booksRecyclerView: RecyclerView

    private var categoryNames = arrayListOf<String>()
    private var demoBookNames = arrayListOf<String>()


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        essentialRecyclerView = root.findViewById(R.id.essentials_topchart_recycler_view) as RecyclerView
        categoryRecyclerView = root.findViewById(R.id.recycler_view) as RecyclerView
        booksRecyclerView = root.findViewById(R.id.recycler_view2) as RecyclerView


        /*getting data from string resource, and converting them into ArrayList*/
        categoryNames = resources.getStringArray(R.array.category_names).toCollection(ArrayList())
        demoBookNames = resources.getStringArray(R.array.demo_book_names).toCollection(ArrayList())

        ih = ItemHelper()
        ih.setGetAllBooksSuccessListener(this)
        ih.setGetAllBooksFailureListener(this)
        ih.setGetAllEssentialsSuccessListener(this)
        ih.setGetAllEssentialsFailureListener(this)

        mContext = this.context!!

        // FAB
        val fab: View = root.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            navController.navigate(R.id.navigation_cart)
        }

        return root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        initRecyclerView(mContext)
        super.onStart()
    }

    private fun initRecyclerView(context: Context){

        val categoryAdapter = RecycleViewAdapter(
            this.context,
            categoryNames,
            this
        )

        categoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        categoryRecyclerView.adapter = categoryAdapter

        ih.getAllBooks()
        ih.getAllEssentials()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun getAllBooksSuccess(bookArray: ArrayList<Book>) {
        val booksAdapter = GroupAdapter<GroupieViewHolder>()
        bookArray.forEach {
            booksAdapter.add(BookAdapter(it))
        }
        booksRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        booksRecyclerView.adapter = booksAdapter
        val listener = ItemOnClickListener(mContext)
        listener.setOnExitListener(this)
        booksAdapter.setOnItemClickListener(listener)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun getAllBooksFailure() {
        Toast.makeText(mContext, "Failed to get all books", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun getAllEssentialsSuccess(essentialArray: ArrayList<Essential>) {
        val essentialAdapter = GroupAdapter<GroupieViewHolder>()
        essentialArray.forEach {
            essentialAdapter.add(EssentialAdapter(it))
        }
        essentialRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        essentialRecyclerView.adapter = essentialAdapter
        val listener = ItemOnClickListener(mContext)
        listener.setOnExitListener(this)
        essentialAdapter.setOnItemClickListener(listener)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun getAllEssentialsFailure() {
        Toast.makeText(mContext, "Failed to get all essentials", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }


    override fun onCatClick(name: String) {
        Toast.makeText(this.context, name, Toast.LENGTH_SHORT).show()
        when(name) {
            "Undergraduate" -> navController.navigate(R.id.action_navigation_home_to_undergraduateBrowseFragment)
            "Postgraduate" -> navController.navigate(R.id.action_navigation_home_to_postGraduateBrowseFragment)
            "English Medium" -> navController.navigate(R.id.action_navigation_home_to_englishMediumBrowseFragment)
            "NCTB" -> navController.navigate(R.id.action_navigation_home_to_nctbBrowseFragment)
            "Abroad" -> navController.navigate(R.id.action_navigation_home_to_abroadBrowseFragment)
            "Literature" -> navController.navigate(R.id.action_navigation_home_to_literatureBrowseFragment)
        }
    }
}
