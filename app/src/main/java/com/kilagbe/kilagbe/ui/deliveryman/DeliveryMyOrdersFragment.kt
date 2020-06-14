package com.kilagbe.kilagbe.ui.deliveryman

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
import com.kilagbe.kilagbe.tools.DeliverymanMyOrderAdapter
import com.kilagbe.kilagbe.tools.DeliverymanOrderItemOnClickListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

/**
 * A simple [Fragment] subclass.
 */
class DeliveryMyOrdersFragment : Fragment(), OrderHelper.getOrdersSuccessListener, OrderHelper.getOrdersFailureListener {

    private lateinit var oh: OrderHelper
    private lateinit var ph: ProfileHelper

    lateinit var mContext: Context

    private lateinit var myOrdersRecycler: RecyclerView

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        oh = OrderHelper()
        oh.setGetOrdersFailureListener(this)
        oh.setGetOrdersSuccessListener(this)

        val root = inflater.inflate(R.layout.fragment_delivery_my_orders, container, false)
        myOrdersRecycler = root.findViewById(R.id.my_orders_recycler_view)

        mContext = this.context!!

        return root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        initRecyclerView()
        super.onStart()
    }

    private fun initRecyclerView() {
        ph = ProfileHelper()
        oh.getDeliverymanPersonalOrders(ph.getUid().toString())
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun getOrdersSuccess(orderArray: ArrayList<CompleteOrder>) {
        val context = mContext
        val adapter = GroupAdapter<GroupieViewHolder>()

        orderArray.forEach {
            adapter.add(DeliverymanMyOrderAdapter(it, mContext))
        }
        val listener = DeliverymanOrderItemOnClickListener(mContext)
        adapter.setOnItemClickListener(listener)
        myOrdersRecycler.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL ,false)
        myOrdersRecycler.adapter = adapter
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun getOrdersFailure() {
        Toast.makeText(mContext, "Failed to get orders", Toast.LENGTH_SHORT).show()
    }
}
