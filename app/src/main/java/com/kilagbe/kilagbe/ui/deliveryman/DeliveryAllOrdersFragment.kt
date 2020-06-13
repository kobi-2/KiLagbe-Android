package com.kilagbe.kilagbe.ui.deliveryman

import android.annotation.SuppressLint
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
import com.kilagbe.kilagbe.tools.DeliverymanOrderAdapter
import com.kilagbe.kilagbe.tools.DeliverymanOrderItemOnClickListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

/**
 * A simple [Fragment] subclass.
 */
class DeliveryAllOrdersFragment : Fragment(), OrderHelper.getOrdersSuccessListener, OrderHelper.getOrdersFailureListener {

    lateinit var oh: OrderHelper

    private lateinit var allOrdersRecycler: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        oh = OrderHelper()
        oh.setGetOrdersFailureListener(this)
        oh.setGetOrdersSuccessListener(this)
        val root = inflater.inflate(R.layout.fragment_delivery_all_orders, container, false)
        allOrdersRecycler = root.findViewById(R.id.all_orders_recycler_view)

        return root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        initRecyclerView()
        super.onStart()
    }

    private fun initRecyclerView() {
        oh.getAllOrders()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun getOrdersSuccess(orderArray: ArrayList<CompleteOrder>) {
        val context = this.activity!!
        val adapter = GroupAdapter<GroupieViewHolder>()

        orderArray.forEach {
            adapter.add(DeliverymanOrderAdapter(it, context))
        }
        val listener = DeliverymanOrderItemOnClickListener(context)
        adapter.setOnItemClickListener(listener)
        allOrdersRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
        allOrdersRecycler.adapter = adapter
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun getOrdersFailure() {
        Toast.makeText(this.activity!!, "Failed to get orders", Toast.LENGTH_SHORT).show()
    }
}
