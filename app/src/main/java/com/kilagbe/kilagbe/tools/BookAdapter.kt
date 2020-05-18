package com.kilagbe.kilagbe.tools

import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Book
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.recycle_view_layout.view.*

class BookAdapter(val book: Book) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.recycle_view_layout
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.text_view.setText("${book.name}")
    }
}