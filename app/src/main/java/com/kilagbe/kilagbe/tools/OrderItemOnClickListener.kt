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
import com.kilagbe.kilagbe.data.Book
import com.kilagbe.kilagbe.data.Cart
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
                        if (q < book!!.amountInStock!!) {
                            q++
                            dialogview.findViewById<TextView>(R.id.quantity_text).setText(q.toString())
                        } else {
                            Toast.makeText(context, "Exceeding quantity in stock", Toast.LENGTH_SHORT).show()
                        }
                    }

                    dialogview.findViewById<Button>(R.id.dec_button).setOnClickListener {
                        var q = dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt()
                        if (q > 0) {
                            q--
                            dialogview.findViewById<TextView>(R.id.quantity_text).setText(q.toString())
                        } else {
                            Toast.makeText(context, "Quantity has to be greater than 0", Toast.LENGTH_SHORT).show()
                        }
                    }

                    dialogview.findViewById<Button>(R.id.modify_button).setOnClickListener {
                        ch.modifyCartBook(item.order.itemid, dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt(), FirebaseAuth.getInstance().uid.toString())
//                        modifyItemBook(FirebaseAuth.getInstance().uid.toString(), item.order.itemid, dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt())
//                        val inc = dialogview.findViewById<TextView>(R.id.itemQty).text.toString().toInt() - dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt()
//                        FirebaseFirestore.getInstance().collection("books").document(item.order.itemid).update("amountInStock", FieldValue.increment(inc.toLong()))
//                            .addOnSuccessListener {
//                                Toast.makeText(context, "Updated item in database", Toast.LENGTH_SHORT).show()
//                            }
//                            .addOnFailureListener {
//                                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
//                            }
                    }

                    dialogview.findViewById<Button>(R.id.delete_button).setOnClickListener {
                        ch.deleteCartBook(item.order.itemid, FirebaseAuth.getInstance().uid.toString())
//                        deleteItemBook(FirebaseAuth.getInstance().uid.toString(), item.order.itemid)
//                        val inc = dialogview.findViewById<TextView>(R.id.itemQty).text.toString().toInt()
//                        FirebaseFirestore.getInstance().collection("books").document(item.order.itemid).update("amountInStock", FieldValue.increment(inc.toLong()))
//                            .addOnSuccessListener {
//                                Toast.makeText(context, "Updated item in database", Toast.LENGTH_SHORT).show()
//                            }
//                            .addOnFailureListener {
//                                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
//                            }
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
                            if (q < book!!.amountInStock!!) {
                                q++
                                dialogview.findViewById<TextView>(R.id.quantity_text)
                                    .setText(q.toString())
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
                                dialogview.findViewById<TextView>(R.id.quantity_text)
                                    .setText(q.toString())
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
//                            modifyItemEssential(FirebaseAuth.getInstance().uid.toString(), item.order.itemid, dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt())
//                            val inc = dialogview.findViewById<TextView>(R.id.itemQty).text.toString().toInt() - dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt()
//                            FirebaseFirestore.getInstance().collection("essentials").document(item.order.itemid).update("amountInStock", FieldValue.increment(inc.toLong()))
//                                .addOnSuccessListener {
//                                    Toast.makeText(context, "Updated item in database", Toast.LENGTH_SHORT).show()
//                                }
//                                .addOnFailureListener {
//                                    Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
//                                }
                        }

                        dialogview.findViewById<Button>(R.id.delete_button).setOnClickListener {
                            ch.deleteCartEssential(item.order.itemid, FirebaseAuth.getInstance().uid.toString())
//                            deleteItemEssential(FirebaseAuth.getInstance().uid.toString(), item.order.itemid)
//                            val inc = dialogview.findViewById<TextView>(R.id.itemQty).text.toString().toInt()
//                            FirebaseFirestore.getInstance().collection("essentials").document(item.order.itemid).update("amountInStock", FieldValue.increment(inc.toLong()))
//                                .addOnSuccessListener {
//                                    Toast.makeText(context, "Updated item in database", Toast.LENGTH_SHORT).show()
//                                }
//                                .addOnFailureListener {
//                                    Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
//                                }
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

    fun deleteItemBook(uid: String, itemid: String)
    {
        val dbref = FirebaseFirestore.getInstance().collection("carts").document(uid)
        dbref.get()
            .addOnSuccessListener {
                if (it!!.exists()) {
                    //check to see if item has been previously added to cart
                    val cart = it.toObject(Cart::class.java)
                    if ( (cart!!.orderBookItems.filter { it.itemid == itemid }).isNotEmpty() )
                    {
                        val oldList = cart!!.orderBookItems
                        val ind = oldList.indexOfFirst {
                            it.itemid == itemid
                        }
                        val subtract = oldList[ind].cost
                        val add = 0.0
                        val inc = add?.minus(subtract!!)
                        oldList.removeAt(ind)
                        dbref.update("orderBookItems", oldList)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
                                dbref.update("total", FieldValue.increment(inc.toLong()))
                                    .addOnSuccessListener {
                                        dialog.dismiss()
                                        mOnExitListener.onExit()
                                    }
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

//    fun modifyItemBook(uid: String, itemid: String, qty: Int)
//    {
//        val dbref = FirebaseFirestore.getInstance().collection("carts").document(uid)
//        dbref.get()
//            .addOnSuccessListener {
//                if (it!!.exists()) {
//                    //check to see if item has been previously added to cart
//                    val cart = it.toObject(Cart::class.java)
//                    if ( (cart!!.orderBookItems.filter { it.itemid == itemid }).isNotEmpty() )
//                    {
//                        val oldList = cart!!.orderBookItems
//                        val ind = oldList.indexOfFirst {
//                            it.itemid == itemid
//                        }
//                        val add = oldList[ind].cost?.div(oldList[ind].qty!!)?.times(qty)
//                        val subtract = oldList[ind].cost
//                        val inc = add?.minus(subtract!!)
//                        oldList[ind].qty = qty
//                        oldList[ind].cost = add
//                        dbref.update("orderBookItems", oldList)
//                            .addOnSuccessListener {
//                                Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_SHORT).show()
//                                dbref.update("total", FieldValue.increment(inc?.toLong()!!))
//                                    .addOnSuccessListener {
//                                        dialog.dismiss()
//                                        mOnExitListener.onExit()
//                                    }
//                            }
//                            .addOnFailureListener {
//                                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
//                            }
//                    }
//                    else {
//                        Toast.makeText(context, "Order not found in cart", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(context, "Cart not found", Toast.LENGTH_SHORT).show()
//                }
//            }
//            .addOnFailureListener {
//                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
//            }
//    }

    fun deleteItemEssential(uid: String, itemid: String)
    {
        val dbref = FirebaseFirestore.getInstance().collection("carts").document(uid)
        dbref.get()
            .addOnSuccessListener {
                if (it!!.exists()) {
                    //check to see if item has been previously added to cart
                    val cart = it.toObject(Cart::class.java)
                    if ( (cart!!.orderEssentialItems.filter { it.itemid == itemid }).isNotEmpty() )
                    {
                        val oldList = cart!!.orderEssentialItems
                        val ind = oldList.indexOfFirst {
                            it.itemid == itemid
                        }
                        val subtract = oldList[ind].cost
                        val add = 0.0
                        val inc = add?.minus(subtract!!)
                        oldList.removeAt(ind)
                        dbref.update("orderEssentialItems", oldList)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
                                dbref.update("total", FieldValue.increment(inc.toLong()))
                                    .addOnSuccessListener {
                                        dialog.dismiss()
                                        mOnExitListener.onExit()
                                    }
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

//    fun modifyItemEssential(uid: String, itemid: String, qty: Int)
//    {
//        val dbref = FirebaseFirestore.getInstance().collection("carts").document(uid)
//        dbref.get()
//            .addOnSuccessListener {
//                if (it!!.exists()) {
//                    //check to see if item has been previously added to cart
//                    val cart = it.toObject(Cart::class.java)
//                    if ( (cart!!.orderEssentialItems.filter { it.itemid == itemid }).isNotEmpty() )
//                    {
//                        val oldList = cart!!.orderEssentialItems
//                        val ind = oldList.indexOfFirst {
//                            it.itemid == itemid
//                        }
//                        val add = oldList[ind].cost?.div(oldList[ind].qty!!)?.times(qty)
//                        val subtract = oldList[ind].cost
//                        val inc = add?.minus(subtract!!)
//                        oldList[ind].qty = qty
//                        oldList[ind].cost = add
//                        dbref.update("orderEssentialItems", oldList)
//                            .addOnSuccessListener {
//                                Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_SHORT).show()
//                                dbref.update("total", FieldValue.increment(inc?.toLong()!!))
//                                    .addOnSuccessListener {
//                                        dialog.dismiss()
//                                        mOnExitListener.onExit()
//                                    }
//                            }
//                            .addOnFailureListener {
//                                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
//                            }
//                    }
//                    else {
//                        Toast.makeText(context, "Order not found in cart", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(context, "Cart not found", Toast.LENGTH_SHORT).show()
//                }
//            }
//            .addOnFailureListener {
//                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
//            }
//    }

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