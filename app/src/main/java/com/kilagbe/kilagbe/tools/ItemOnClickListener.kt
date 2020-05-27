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
import com.kilagbe.kilagbe.data.Essential
import com.kilagbe.kilagbe.data.OrderItems
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener

class ItemOnClickListener(val context: Context) : OnItemClickListener{
    lateinit var dialog: AlertDialog
    lateinit var layoutInflater: LayoutInflater
    override fun onItemClick(item: Item<*>, view: View) {
        if ( item is BookAdapter )
        {
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
                addToCartBook(item.book, dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt(), FirebaseAuth.getInstance().uid!!)
            }
            dialog.setView(dialogview)
        }
        else if ( item is EssentialAdapter )
        {
            layoutInflater = LayoutInflater.from(context)
            dialog = AlertDialog.Builder(context).create()
            val dialogview = layoutInflater.inflate(R.layout.essential_display, null)
            Picasso.get().load(item.essential.photoUrl).into(dialogview.findViewById<ImageView>(R.id.essentialMainImg))
            dialogview.findViewById<TextView>(R.id.essentialName).setText("${item.essential.name}")
            dialogview.findViewById<TextView>(R.id.essentialStock).setText("${item.essential.amountInStock.toString()}")
            dialogview.findViewById<TextView>(R.id.essentialManufacturer).setText("${item.essential.manufacturer}")

            dialogview.findViewById<Button>(R.id.inc_button).setOnClickListener {
                var q = dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt()
                if ( q < item.essential.amountInStock!! )
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
                addToCartEssential(item.essential, dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt(), FirebaseAuth.getInstance().uid!!)
            }
            dialog.setView(dialogview)
        }
        dialog.setCancelable(true)
        dialog.show()
    }

    fun addToCartBook(book: Book, qty: Int, uid: String)
    {
        if ( qty > 0 ) {
            val order = OrderItems(book.itemId!!)
            order.qty = qty
            order.cost = qty * book.price
            val dbref = FirebaseFirestore.getInstance().collection("carts").document(uid)
            dbref.get()
                .addOnSuccessListener {
                    if (it!!.exists()) {
                        //check to see if item has been previously added to cart
                        val cart = it.toObject(Cart::class.java)
                        if ( (cart!!.orderBookItems.filter { it.itemid == book.itemId }).isNotEmpty() )
                        {
                            val oldList = cart!!.orderBookItems
                            val ind = oldList.indexOfFirst {
                                it.itemid == book.itemId
                            }
                            oldList[ind].qty = oldList[ind].qty?.plus(qty)
                            oldList[ind].cost = oldList[ind].cost?.plus(order.cost!!)
                            dbref.update("orderBookItems", oldList)
                                .addOnSuccessListener {
                                    val inc = -1 * qty
                                    FirebaseFirestore.getInstance().collection("books").document(order.itemid).update("amountInStock", FieldValue.increment(inc.toLong()))
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Updated item in database", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        else {
                            dbref.update("orderBookItems", FieldValue.arrayUnion(order))
                                .addOnSuccessListener {
                                    val inc = -1 * qty
                                    FirebaseFirestore.getInstance().collection("books").document(order.itemid).update("amountInStock", FieldValue.increment(inc.toLong()))
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Updated item in database", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        dbref.update("total", FieldValue.increment(order.cost!!.toLong()))
                            .addOnSuccessListener {
                                Toast.makeText(context, "Updated total for cart", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        val cart = Cart(uid)
                        cart.orderBookItems.add(order)
                        cart.status = "ORDERING"
                        cart.total = cart.total?.plus(order.cost!!)
                        dbref.set(cart)
                            .addOnSuccessListener {
                                val inc = -1 * qty
                                FirebaseFirestore.getInstance().collection("books").document(order.itemid).update("amountInStock", FieldValue.increment(inc.toLong()))
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Updated item in database", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                                    }
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

    fun addToCartEssential(essential: Essential, qty: Int, uid: String)
    {
        if ( qty > 0 ) {
            val order = OrderItems(essential.itemId!!)
            order.cost = qty * essential.price
            order.qty = qty
            val dbref = FirebaseFirestore.getInstance().collection("carts").document(uid)
            dbref.get()
                .addOnSuccessListener {
                    if (it!!.exists()) {
                        //check to see if item has been previously added to cart
                        val cart = it.toObject(Cart::class.java)
                        if ( (cart!!.orderEssentialItems.filter { it.itemid == essential.itemId }).isNotEmpty() )
                        {
                            val oldList = cart!!.orderEssentialItems
                            val ind = oldList.indexOfFirst {
                                it.itemid == essential.itemId
                            }
                            oldList[ind].qty = oldList[ind].qty?.plus(qty)
                            oldList[ind].cost = oldList[ind].cost?.plus(order.cost!!)
                            dbref.update("orderEssentialItems", oldList)
                                .addOnSuccessListener {
                                    val inc = -1 * qty
                                    FirebaseFirestore.getInstance().collection("essentials").document(order.itemid).update("amountInStock", FieldValue.increment(inc.toLong()))
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Updated item in database", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        else {
                            dbref.update("orderEssentialItems", FieldValue.arrayUnion(order))
                                .addOnSuccessListener {
                                    val inc = -1 * qty
                                    FirebaseFirestore.getInstance().collection("essentials").document(order.itemid).update("amountInStock", FieldValue.increment(inc.toLong()))
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Updated item in database", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                                        }

                                    Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        dbref.update("total", FieldValue.increment(order.cost!!.toLong()))
                            .addOnSuccessListener {
                                Toast.makeText(context, "Updated total for cart", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        val cart = Cart(uid)
                        cart.orderEssentialItems.add(order)
                        cart.status = "ORDERING"
                        cart.total = cart.total?.plus(order.cost!!)
                        dbref.set(cart)
                            .addOnSuccessListener {
                                val inc = -1 * qty
                                FirebaseFirestore.getInstance().collection("essentials").document(order.itemid).update("amountInStock", FieldValue.increment(inc.toLong()))
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Updated item in database", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                                    }
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