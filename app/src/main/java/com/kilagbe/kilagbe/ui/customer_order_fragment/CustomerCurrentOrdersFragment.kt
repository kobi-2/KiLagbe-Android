package com.kilagbe.kilagbe.ui.customer_order_fragment

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
import com.kilagbe.kilagbe.data.CompleteOrder
import com.kilagbe.kilagbe.databasing.OrderHelper
import com.kilagbe.kilagbe.databasing.ProfileHelper
import com.kilagbe.kilagbe.tools.CustomerCurrentCompleteOrder
import com.kilagbe.kilagbe.tools.CustomerOrderItemOnClickListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class CustomerCurrentOrdersFragment : Fragment(), OrderHelper.getOrdersSuccessListener, OrderHelper.getOrdersFailureListener {

    lateinit var currentOrdersRecycler: RecyclerView
    lateinit var mContext: Context

    lateinit var oh: OrderHelper
    lateinit var ph: ProfileHelper

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_customer_current_orders, container, false)

        currentOrdersRecycler = root.findViewById(R.id.current_orders_recyclerview)

        oh = OrderHelper()
        ph = ProfileHelper()

        oh.setGetOrdersSuccessListener(this)
        oh.setGetOrdersFailureListener(this)

        mContext = this.context!!

        return root
    }

    override fun onStart() {
        initRecyclerView()
        super.onStart()
    }

    private fun initRecyclerView()
    {
        oh.getCustomerPersonalCurrentOrders(ph.getUid().toString())
    }

    override fun getOrdersSuccess(orderArray: ArrayList<CompleteOrder>) {
        val adapter = GroupAdapter<GroupieViewHolder>()
        orderArray.forEach {
            adapter.add(CustomerCurrentCompleteOrder(it))
        }
        val listener = CustomerOrderItemOnClickListener(mContext)
        adapter.setOnItemClickListener(listener)
        currentOrdersRecycler.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL ,false)
        currentOrdersRecycler.adapter = adapter
    }

    override fun getOrdersFailure() {
        Toast.makeText(mContext, "Failed to get orders", Toast.LENGTH_SHORT).show()
    }
}