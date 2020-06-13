package com.kilagbe.kilagbe.tools

import android.content.Context
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.CompleteOrder
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.recyclerview_delivery_order_list.view.*

class DeliverymanOrderAdapter(val orderItem: CompleteOrder, val context: Context) : Item<GroupieViewHolder>()
{
    override fun getLayout(): Int {
        return R.layout.recyclerview_delivery_order_list
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.order_no_textview.text = orderItem.orderId
//        viewHolder.itemView.phone_no_textview.text = orderItem.customerphone
        viewHolder.itemView.address_textview.text = orderItem.address
    }
}