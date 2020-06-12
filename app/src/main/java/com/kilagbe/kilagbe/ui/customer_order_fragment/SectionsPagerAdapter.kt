package com.kilagbe.kilagbe.ui.customer_order_fragment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionsPagerAdapter( fm: FragmentManager) : FragmentPagerAdapter(fm){

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 ->  CustomerCurrentOrdersFragment()
            else -> CustomerPastOrdersFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0-> "Current Orders"
            else -> "Past Orders"
        }
    }

    override fun getCount(): Int {
        return 2
    }

}
