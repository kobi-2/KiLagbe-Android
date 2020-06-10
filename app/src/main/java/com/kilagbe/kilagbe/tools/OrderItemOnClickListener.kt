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
import com.kilagbe.kilagbe.data.Book
import com.kilagbe.kilagbe.databasing.CartHelper
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener


class OrderItemOnClickListener(val context: Context) : OnItemClickListener, CartHelper.updateCartSuccessListener, CartHelper.updateCartFailureListener, CartHelper.cartNotFoundFailureListener
{
    lateinit var dialog: AlertDialog
    lateinit var mOnExitListener: onExitListener
    lateinit var layoutInflater: LayoutInflater

    val ch = CartHelper(context)

    override fun onItemClick(item: Item<*>, view: View) {
        ch.setCartNotFoundFailureListener(this)
        ch.setUpdateCartFailureListener(this)
        ch.setUpdateCartSuccessListener(this)

        item as CustomerOrderAdapter
        layoutInflater = LayoutInflater.from(context)

        FirebaseFirestore.getInstance().collection("books").whereEqualTo("itemId", item.order.itemid).get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    dialog = AlertDialog.Builder(context).create()
                    val dialogview = layoutInflater.inflate(R.layout.order_item_display, null)
                    val book = it.documents[0].toObject(Book::class.java)

                    Picasso.get().load(book!!.photoUrl!!).into(dialogview.findViewById<ImageView>(R.id.itemImg))
                    dialogview.findViewById<TextView>(R.id.itemName).text = book.name
                    dialogview.findViewById<TextView>(R.id.itemQty).text = item.order.qty.toString()
                    dialogview.findViewById<TextView>(R.id.quantity_text).text = item.order.qty.toString()

                    dialogview.findViewById<Button>(R.id.inc_button).setOnClickListener {
                        var q = dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt()
                        if (q < book.amountInStock!!) {
                            q++
                            dialogview.findViewById<TextView>(R.id.quantity_text).text = q.toString()
                        } else {
                            Toast.makeText(context, "Exceeding quantity in stock", Toast.LENGTH_SHORT).show()
                        }
                    }

                    dialogview.findViewById<Button>(R.id.dec_button).setOnClickListener {
                        var q = dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt()
                        if (q > 0) {
                            q--
                            dialogview.findViewById<TextView>(R.id.quantity_text).text = q.toString()
                        } else {
                            Toast.makeText(context, "Quantity has to be greater than 0", Toast.LENGTH_SHORT).show()
                        }
                    }

                    dialogview.findViewById<Button>(R.id.modify_button).setOnClickListener {
                        ch.modifyCartBook(item.order.itemid, dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt(), FirebaseAuth.getInstance().uid.toString())
                    }

                    dialogview.findViewById<Button>(R.id.delete_button).setOnClickListener {
                        ch.deleteCartBook(item.order.itemid, FirebaseAuth.getInstance().uid.toString())
                    }

                    dialog.setView(dialogview)
                    dialog.setCancelable(true)
                    dialog.show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
            }

            FirebaseFirestore.getInstance().collection("essentials").whereEqualTo("itemId", item.order.itemid).get()
                .addOnSuccessListener {
                    if (!it.isEmpty) {
                        dialog = AlertDialog.Builder(context).create()
                        val dialogview = layoutInflater.inflate(R.layout.order_item_display, null)
                        val book = it.documents[0].toObject(Book::class.java)
                        Picasso.get().load(book!!.photoUrl!!).into(dialogview.findViewById<ImageView>(R.id.itemImg))
                        dialogview.findViewById<TextView>(R.id.itemName).text = book.name
                        dialogview.findViewById<TextView>(R.id.itemQty).text = item.order.qty.toString()
                        dialogview.findViewById<TextView>(R.id.quantity_text).text = item.order.qty.toString()
                        dialogview.findViewById<Button>(R.id.inc_button).setOnClickListener {
                            var q =
                                dialogview.findViewById<TextView>(R.id.quantity_text).text.toString()
                                    .toInt()
                            if (q < book.amountInStock!!) {
                                q++
                                dialogview.findViewById<TextView>(R.id.quantity_text).text = q.toString()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Exceeding quantity in stock",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        dialogview.findViewById<Button>(R.id.dec_button).setOnClickListener {
                            var q =
                                dialogview.findViewById<TextView>(R.id.quantity_text).text.toString()
                                    .toInt()
                            if (q > 0) {
                                q--
                                dialogview.findViewById<TextView>(R.id.quantity_text).text = q.toString()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Quantity has to be greater than 0",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        dialogview.findViewById<Button>(R.id.modify_button).setOnClickListener {
                            ch.modifyCartEssential(item.order.itemid, dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt(), FirebaseAuth.getInstance().uid.toString())
                        }

                        dialogview.findViewById<Button>(R.id.delete_button).setOnClickListener {
                            ch.deleteCartEssential(item.order.itemid, FirebaseAuth.getInstance().uid.toString())
                        }

                        dialog.setView(dialogview)
                        dialog.setCancelable(true)
                        dialog.show()
                    }
                }
                        .addOnFailureListener {
                            Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }

    fun setOnExitListener(lol: onExitListener)
    {
        this.mOnExitListener = lol
    }

    interface onExitListener {
        fun onExit()
    }

    override fun updateCartSuccess() {
        Toast.makeText(context, "Updated cart successfully", Toast.LENGTH_SHORT).show()
        mOnExitListener.onExit()
        dialog.dismiss()
    }

    override fun updateCartFailure() {
        Toast.makeText(context, "Failed to update cart", Toast.LENGTH_SHORT).show()
    }

    override fun cartNotFoundFailure() {
        Toast.makeText(context, "Failed to get cart", Toast.LENGTH_SHORT).show()
    }
}