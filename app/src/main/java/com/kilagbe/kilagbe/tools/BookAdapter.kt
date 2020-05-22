package com.kilagbe.kilagbe.tools

import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Book
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.recyclerview_generic_item.view.*

class BookAdapter(val book: Book) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.recyclerview_generic_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.item_name_textview.setText("${book.name}")
        Picasso.get().load(book.photoUrl.toString()).into(viewHolder.itemView.item_imageview)
    }
}