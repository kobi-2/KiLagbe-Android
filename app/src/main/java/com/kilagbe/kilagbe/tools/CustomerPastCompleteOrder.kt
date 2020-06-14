package com.kilagbe.kilagbe.tools

import android.widget.TextView
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.CompleteOrder
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class CustomerPastCompleteOrder(val orderItem: CompleteOrder) : Item<GroupieViewHolder>()
{
    override fun getLayout(): Int {
        return R.layout.recyclerview_customer_past_order_list
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.order_no_textview).text = orderItem.orderId
        viewHolder.itemView.findViewById<TextView>(R.id.deliveryman_phone_no_textview).text = orderItem.deliverymanphone
        viewHolder.itemView.findViewById<TextView>(R.id.total_price_textview).text = orderItem.cart!!.total!!.toString()
    }
}