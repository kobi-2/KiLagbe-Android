package com.kilagbe.kilagbe.tools

import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Book
import com.kilagbe.kilagbe.data.OrderItems
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.recyclerview_order_item.view.*

class OrderItemAdapter(val order: OrderItems) : Item<GroupieViewHolder>()
{
    override fun getLayout(): Int {
        return R.layout.recyclerview_order_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        FirebaseFirestore.getInstance().collection("books").whereEqualTo("itemId", order.itemid).get()
            .addOnSuccessListener {
                val temp = it.documents[0].toObject(Book::class.java)
                viewHolder.itemView.itemTitle.text = temp!!.name
                viewHolder.itemView.itemQty.text = order.qty.toString()
                Picasso.get().load(temp!!.photoUrl).into(viewHolder.itemView.itemImg)
            }
    }
}