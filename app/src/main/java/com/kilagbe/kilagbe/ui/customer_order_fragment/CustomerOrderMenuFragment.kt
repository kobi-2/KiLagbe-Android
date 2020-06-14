package com.kilagbe.kilagbe.ui.customer_order_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.kilagbe.kilagbe.R

class CustomerOrderMenuFragment : Fragment() {

    lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_customer_order_menu, container, false)


        viewPager = root.findViewById(R.id.view_pager) as ViewPager
        tabs = root.findViewById(R.id.tab_layout) as TabLayout

        setup()

        return root

    }

    override fun onStart() {
        setup()
        super.onStart()
    }

    private fun setup(){

        sectionsPagerAdapter = SectionsPagerAdapter( requireFragmentManager())

        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }


}