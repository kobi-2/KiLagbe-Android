package com.kilagbe.kilagbe.ui.home

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
import com.kilagbe.kilagbe.RecycleViewAdapter
import com.kilagbe.kilagbe.RecycleViewAdapter.OnCatListener


class HomeFragment : Fragment(), OnCatListener {

    /*
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_category)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
    */


//  variables
    private lateinit var navController : NavController

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var booksRecyclerView: RecyclerView
    private lateinit var categoryAdapter: RecycleViewAdapter
    private lateinit var booksAdapter: RecycleViewAdapter

    private var categoryNames = arrayListOf<String>()
    private var demoBookNames = arrayListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)


        categoryRecyclerView = root.findViewById(R.id.recycler_view) as RecyclerView
        booksRecyclerView = root.findViewById(R.id.recycler_view2) as RecyclerView


        /*getting data from string resource, and converting them into ArrayList*/
        categoryNames = resources.getStringArray(R.array.category_names).toCollection(ArrayList())
        demoBookNames = resources.getStringArray(R.array.demo_book_names).toCollection(ArrayList())

        initRecyclerView()


        return root

    }



    private fun initRecyclerView(){

        categoryAdapter = RecycleViewAdapter(this.context, categoryNames, this)
        booksAdapter = RecycleViewAdapter(this.context, demoBookNames, this)

        categoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        categoryRecyclerView.adapter = categoryAdapter

        booksRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        booksRecyclerView.adapter = booksAdapter

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }


    override fun onCatClick(name: String) {
        Toast.makeText(this.context, name, Toast.LENGTH_SHORT).show()
        when(name) {
            "Undergraduate" -> navController!!.navigate(R.id.action_navigation_home_to_undergraduateBrowseFragment)
            "Post Graduate" -> navController!!.navigate(R.id.action_navigation_home_to_postGraduateBrowseFragment)
            "English Medium" -> navController!!.navigate(R.id.action_navigation_home_to_englishMediumBrowseFragment)
            "NCTB" -> navController!!.navigate(R.id.action_navigation_home_to_nctbBrowseFragment)
            "Abroad" -> navController!!.navigate(R.id.action_navigation_home_to_abroadBrowseFragment)
        }
    }

}
