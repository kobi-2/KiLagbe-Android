package com.kilagbe.kilagbe.tools

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.databasing.OrderHelper
import com.kilagbe.kilagbe.databasing.ProfileHelper
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener

class DeliverymanOrderItemOnClickListener(val context: Context) : OnItemClickListener, OrderHelper.confirmOrderSuccessListener, OrderHelper.confirmOrderFailureListener
{
    lateinit var dialog: AlertDialog
    lateinit var layoutInflater: LayoutInflater
    lateinit var recyclerView: RecyclerView

    lateinit var ph: ProfileHelper
    lateinit var oh: OrderHelper

    override fun onItemClick(item: Item<*>, view: View) {
        item as DeliverymanOrderAdapter

        dialog = AlertDialog.Builder(context).create()
        layoutInflater = LayoutInflater.from(context)
        ph = ProfileHelper()
        oh = OrderHelper()

        val uid = ph.getUid()
        oh.setConfirmOrderSuccessListener(this)
        oh.setConfirmOrderFailureListener(this)

        val dialogview = layoutInflater.inflate(R.layout.alert_dialog_deliveryman_order_details, null)
        dialogview.findViewById<TextView>(R.id.order_no_textview).text = item.orderItem.orderId
        dialogview.findViewById<TextView>(R.id.phone_no_textview).text = item.orderItem.customerphone
        dialogview.findViewById<TextView>(R.id.address_textview).text = item.orderItem.address

        recyclerView = dialogview.findViewById<RecyclerView>(R.id.item_list_recycler_view)

        val adapter = GroupAdapter<GroupieViewHolder>()
        item.orderItem.cart!!.orderBookItems.forEach {
            adapter.add(DeliverymanOrderDetailsAdapter(it, context))
        }

        item.orderItem.cart!!.orderEssentialItems.forEach {
            adapter.add(DeliverymanOrderDetailsAdapter(it, context))
        }

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
        recyclerView.adapter = adapter

        dialogview.findViewById<TextView>(R.id.grand_total_text_view).text = item.orderItem.cart!!.total.toString()

        dialogview.findViewById<Button>(R.id.confirm_button).setOnClickListener{
            oh.acceptOrder(item.orderItem.orderId.toString(), uid!!)
        }

        dialog.setView(dialogview)
        dialog.setCancelable(true)
        dialog.show()
    }

    override fun confirmOrderSuccess() {
        Toast.makeText(context, "Confirmed order", Toast.LENGTH_SHORT).show()
        dialog.dismiss()
    }

    override fun confirmOrderFailure() {
        Toast.makeText(context, "Failed to confirm order", Toast.LENGTH_SHORT).show()
    }
}