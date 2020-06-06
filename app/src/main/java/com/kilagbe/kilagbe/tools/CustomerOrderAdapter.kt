package com.kilagbe.kilagbe.tools

import android.content.Context
import android.util.Log
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Book
import com.kilagbe.kilagbe.data.Essential
import com.kilagbe.kilagbe.data.OrderItems
import com.kilagbe.kilagbe.databasing.ItemHelper
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.recyclerview_customer_order_item.view.*

class CustomerOrderAdapter(val order: OrderItems, val context: Context) : Item<GroupieViewHolder>(), ItemHelper.getBookSuccessListener, ItemHelper.getBookFailureListener, ItemHelper.getEssentialSuccessListener, ItemHelper.getEssentialFailureListener
{
    val ih = ItemHelper(context)

    private lateinit var mViewHolder: GroupieViewHolder

    override fun getLayout(): Int {
        return R.layout.recyclerview_customer_order_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        this.mViewHolder = viewHolder

        ih.setGetBookSuccessListener(this)
        ih.setGetBookFailureListener(this)
        ih.setGetEssentialSuccessListener(this)
        ih.setGetEssentialFailureListener(this)

        ih.getBook(order.itemid)
        ih.getEssential(order.itemid)

    }

    override fun getBookSuccess(book: Book) {
        binding(mViewHolder, book.name, order.qty.toString(), book.photoUrl!!)
    }

    override fun getEssentialSuccess(essential: Essential) {
        binding(mViewHolder, essential.name, order.qty.toString(), essential.photoUrl!!)
    }

    override fun getBookFailure() {
        Log.d("CustomerOrderAdapter", "N/A")
    }

    override fun getEssentialFailure() {
        Log.d("CustomerOrderAdapter", "N/A")
    }

    fun binding(viewHolder: GroupieViewHolder, name: String, qty: String, photoUrl: String)
    {
        viewHolder.itemView.itemTitle.text = name
        viewHolder.itemView.itemQty.text = qty
        Picasso.get().load(photoUrl).into(viewHolder.itemView.itemImg)
    }

}