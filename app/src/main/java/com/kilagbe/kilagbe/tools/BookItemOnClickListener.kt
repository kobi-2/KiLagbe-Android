package com.kilagbe.kilagbe.tools

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kilagbe.kilagbe.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener

class BookItemOnClickListener(val context: Context, val layoutInflater: LayoutInflater) : OnItemClickListener{
    override fun onItemClick(item: Item<*>, view: View) {
        item as BookAdapter
        val dialog = AlertDialog.Builder(context).create()
        val dialogview = layoutInflater.inflate(R.layout.book_display, null)
        Picasso.get().load(item.book.photoUrl).into(dialogview.findViewById<ImageView>(R.id.bookMainImg))
        dialogview.findViewById<TextView>(R.id.bookName).setText("${item.book.name}")
        var authors: String? = ""
        item.book.authors.forEach {
            authors += "$it, "
        }
        var cats: String? = ""
        item.book.categories.forEach {
            cats += "$it, "
        }
        dialogview.findViewById<TextView>(R.id.bookAuthors).setText("$authors")
        dialogview.findViewById<TextView>(R.id.bookPublishers).setText("${item.book.publisher}")
        dialogview.findViewById<TextView>(R.id.bookCategories).setText("$cats")
        dialogview.findViewById<TextView>(R.id.bookStock).setText("${item.book.amountInStock}")

        dialog.setView(dialogview)
        dialog.setCancelable(true)
        dialog.show()
    }
}