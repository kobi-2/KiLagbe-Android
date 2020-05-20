package com.kilagbe.kilagbe.tools

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Book
import com.kilagbe.kilagbe.data.Cart
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener


class OrderItemOnClickListener(val context: Context, val layoutInflater: LayoutInflater) : OnItemClickListener
{
    lateinit var dialog: AlertDialog

    override fun onItemClick(item: Item<*>, view: View) {
        item as OrderItemAdapter
        Toast.makeText(context, "${item.order.itemid}", Toast.LENGTH_SHORT).show()

        FirebaseFirestore.getInstance().collection("books").whereEqualTo("itemId", item.order.itemid).get()
            .addOnSuccessListener {
                dialog = AlertDialog.Builder(context).create()
                val dialogview = layoutInflater.inflate(R.layout.order_item_display, null)
                val book = it.documents[0].toObject(Book::class.java)
                Picasso.get().load(book!!.photoUrl!!).into(dialogview.findViewById<ImageView>(R.id.itemImg))
                dialogview.findViewById<TextView>(R.id.itemName).text = book.name
                dialogview.findViewById<TextView>(R.id.itemQty).text = item.order.qty.toString()
                dialogview.findViewById<TextView>(R.id.quantity_text).text = item.order.qty.toString()
                dialogview.findViewById<Button>(R.id.inc_button).setOnClickListener {
                    var q = dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt()
                    if ( q < book!!.amountInStock!! )
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

                dialogview.findViewById<Button>(R.id.modify_button).setOnClickListener {
                    modifyItem(FirebaseAuth.getInstance().uid.toString(), item.order.itemid, dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt())
                }

                dialogview.findViewById<Button>(R.id.delete_button).setOnClickListener {
                    deleteItem(FirebaseAuth.getInstance().uid.toString(), item.order.itemid)
                }

                dialog.setView(dialogview)
                dialog.setCancelable(true)
                dialog.show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun deleteItem(uid: String, itemid: String)
    {
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
                        oldList.removeAt(ind)
                        dbref.update("orderItems", oldList)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                    else {
                        Toast.makeText(context, "Order not found in cart", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Cart not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun modifyItem(uid: String, itemid: String, qty: Int)
    {
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
                        oldList[ind].qty = qty
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
                        Toast.makeText(context, "Order not found in cart", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Cart not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}