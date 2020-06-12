package com.kilagbe.kilagbe.ui.customer_order_fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SectionsPagerAdapter( fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

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
