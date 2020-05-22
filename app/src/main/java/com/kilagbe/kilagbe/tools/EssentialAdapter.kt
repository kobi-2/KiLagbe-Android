package com.kilagbe.kilagbe.tools

import android.widget.ImageView
import android.widget.TextView
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Essential
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class EssentialAdapter(val essential: Essential): Item<GroupieViewHolder>()
{
    override fun getLayout(): Int {
        return R.layout.recyclerview_generic_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.item_name_textview).text = essential.name
        Picasso.get().load(essential.photoUrl.toString()).into(viewHolder.itemView.findViewById<ImageView>(R.id.item_imageview))
    }
}