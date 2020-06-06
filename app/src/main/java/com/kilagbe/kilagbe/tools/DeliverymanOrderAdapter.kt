package com.kilagbe.kilagbe.tools

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.CompleteOrder
import com.kilagbe.kilagbe.data.User
import com.kilagbe.kilagbe.databasing.ProfileHelper
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.recyclerview_delivery_order_item.view.*

class DeliverymanOrderAdapter(val orderItem: CompleteOrder, val context: Context) : Item<GroupieViewHolder>(), ProfileHelper.getCustomerSuccessListener, ProfileHelper.getCustomerFailureListener
{
    val ph = ProfileHelper()

    private lateinit var mViewHolder: GroupieViewHolder

    override fun getLayout(): Int {
        return R.layout.recyclerview_delivery_order_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        this.mViewHolder = viewHolder

        ph.setGetCustomerSuccessListener(this)
        ph.setGetCustomerFailureListener(this)

        Log.d("ADAPTER", "${orderItem.customeruid}")
        ph.getCustomer(orderItem.customeruid)
    }

    override fun getCustomerSuccess(customer: User) {
        binding(mViewHolder, customer.phone)
    }

    override fun getCustomerFailure() {
        Toast.makeText(context, "Failed to get user details", Toast.LENGTH_SHORT).show()
    }

    fun binding(viewHolder: GroupieViewHolder, phone: String)
    {
        viewHolder.itemView.order_no_textview.text = orderItem.orderId
        viewHolder.itemView.phone_no_textview.text = phone
        viewHolder.itemView.address_textview.text = orderItem.address
    }
}