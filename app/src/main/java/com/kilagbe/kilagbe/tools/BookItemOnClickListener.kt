package com.kilagbe.kilagbe.tools

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Order
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import java.util.*

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

        dialogview.findViewById<Button>(R.id.inc_button).setOnClickListener {
            var q = dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt()
            if ( q < item.book.amountInStock!! )
            {
                q++
                dialogview.findViewById<TextView>(R.id.quantity_text).setText(q.toString())
            }
            else
            {
                Toast.makeText(context, "Exceeding quantity in stock", Toast.LENGTH_SHORT).show()
            }
        }

        dialogview.findViewById<Button>(R.id.dec_button).setOnClickListener {
            var q = dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt()
            if ( q > 0 )
            {
                q--
                dialogview.findViewById<TextView>(R.id.quantity_text).setText(q.toString())
            }
            else
            {
                Toast.makeText(context, "Quantity has to be greater than 0", Toast.LENGTH_SHORT).show()
            }
        }

        dialogview.findViewById<Button>(R.id.order_button).setOnClickListener {
            order(item.book.itemId!!, dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt())
            dialog.dismiss()
        }

        dialog.setView(dialogview)
        dialog.setCancelable(true)
        dialog.show()
    }

    fun order(itemid: String, qty: Int)
    {
        val orderId = UUID.randomUUID().toString()
        if ( qty > 0 ) {
            val order = Order(orderId, FirebaseAuth.getInstance().currentUser!!.uid, itemid, qty, "Pending")
            FirebaseFirestore.getInstance().collection("orders").document(orderId).set(order)
                .addOnSuccessListener {
                    Toast.makeText(context, "Order posted successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        else
        {
            Toast.makeText(context, "Please enter a quantity greater than 0", Toast.LENGTH_SHORT).show()
        }
    }
}