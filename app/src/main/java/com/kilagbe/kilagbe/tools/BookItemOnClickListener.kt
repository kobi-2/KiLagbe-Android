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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Cart
import com.kilagbe.kilagbe.data.OrderItems
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener

class BookItemOnClickListener(val context: Context) : OnItemClickListener{
    lateinit var dialog: AlertDialog
    lateinit var layoutInflater: LayoutInflater
    override fun onItemClick(item: Item<*>, view: View) {
        item as BookAdapter
        layoutInflater = LayoutInflater.from(context)
        dialog = AlertDialog.Builder(context).create()
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

        dialogview.findViewById<Button>(R.id.addToCart_button).setOnClickListener {
            addToCart(item.book.itemId!!, dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt(), FirebaseAuth.getInstance().uid!!)
        }

        dialog.setView(dialogview)
        dialog.setCancelable(true)
        dialog.show()
    }

    fun addToCart(itemid: String, qty: Int, uid: String)
    {
        if ( qty > 0 ) {
            val order = OrderItems(itemid)
            order.qty = qty
            val dbref = FirebaseFirestore.getInstance().collection("carts").document(uid)
            dbref.get()
                .addOnSuccessListener {
                    if (it!!.exists()) {
                        //check to see if item has been previously added to cart
                        val cart = it.toObject(Cart::class.java)
                        if ( (cart!!.orderItems.filter { it.itemid == itemid }).isNotEmpty() )
                        {
                            val oldList = cart!!.orderItems
                            val ind = oldList.indexOfFirst {
                                it.itemid == itemid
                            }
                            oldList[ind].qty = oldList[ind].qty?.plus(qty)
                            dbref.update("orderItems", oldList)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        else {
                            dbref.update("orderItems", FieldValue.arrayUnion(order))
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        val cart = Cart(uid)
                        cart.orderItems.add(order)
                        cart.status = "ORDERING"
                        dbref.set(cart)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
        }
        else
        {
            Toast.makeText(context, "Please enter a quantity greater than 0", Toast.LENGTH_SHORT).show()
        }
    }
}