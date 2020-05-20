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
import com.kilagbe.kilagbe.tools.BookRecycleViewAdapter
import com.kilagbe.kilagbe.tools.RecycleViewAdapter
import com.kilagbe.kilagbe.tools.RecycleViewAdapter.OnCatListener
import com.kilagbe.kilagbe.data.Book
import com.kilagbe.kilagbe.tools.BookAdapter
import com.kilagbe.kilagbe.tools.BookItemOnClickListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.google.firebase.firestore.FirebaseFirestore





class HomeFragment : Fragment(), OnCatListener{


    private lateinit var navController : NavController

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var booksRecyclerView: RecyclerView
    private lateinit var categoryAdapter: RecycleViewAdapter
//    private lateinit var booksAdapter: RecycleViewAdapter

    private var categoryNames = arrayListOf<String>()
    private var demoBookNames = arrayListOf<String>()


    @SuppressLint("UseRequireInsteadOfGet")
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

        initRecyclerView(this!!.requireActivity())      // using this!!.activity!! gives red lines for some reason


        return root

    }



    private fun initRecyclerView(context: Context){

        categoryAdapter = RecycleViewAdapter(
            this.context,
            categoryNames,
            this
        )

        categoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        categoryRecyclerView.adapter = categoryAdapter




        val booksAdapter = GroupAdapter<GroupieViewHolder>()

        FirebaseFirestore.getInstance().collection("books").get()
            .addOnSuccessListener {
                for ( doc in it!! )
                {
                    val temp = doc.toObject(Book::class.java)
                    booksAdapter.add(BookAdapter(temp))
                }
                booksRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
                booksRecyclerView.adapter = booksAdapter
                val listener = BookItemOnClickListener(context, layoutInflater)
                booksAdapter.setOnItemClickListener(listener)
            }
            .addOnFailureListener {
                Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
            }


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }


    override fun onCatClick(name: String) {
        Toast.makeText(this.context, name, Toast.LENGTH_SHORT).show()
        when(name) {
            "Undergraduate" -> navController!!.navigate(R.id.action_navigation_home_to_undergraduateBrowseFragment)
            "Postgraduate" -> navController!!.navigate(R.id.action_navigation_home_to_postGraduateBrowseFragment)
            "English Medium" -> navController!!.navigate(R.id.action_navigation_home_to_englishMediumBrowseFragment)
            "NCTB" -> navController!!.navigate(R.id.action_navigation_home_to_nctbBrowseFragment)
            "Abroad" -> navController!!.navigate(R.id.action_navigation_home_to_abroadBrowseFragment)
        }
    }


}
