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
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.databasing.CartHelper
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener

class ItemOnClickListener(val context: Context) : OnItemClickListener, CartHelper.updateCartFailureListener, CartHelper.updateCartSuccessListener, CartHelper.cartNotFoundFailureListener, CartHelper.quantityFailureListener{

    lateinit var dialog: AlertDialog
    lateinit var layoutInflater: LayoutInflater
    lateinit var mOnExitListener: onExitListener

    val ch = CartHelper(context)

    override fun onItemClick(item: Item<*>, view: View) {
        ch.setCartNotFoundFailureListener(this)
        ch.setQuantityFailureListener(this)
        ch.setUpdateCartFailureListener(this)
        ch.setUpdateCartSuccessListener(this)

        if ( item is BookAdapter )
        {
            layoutInflater = LayoutInflater.from(context)
            dialog = AlertDialog.Builder(context).create()
            val dialogview = layoutInflater.inflate(R.layout.book_display, null)
            Picasso.get().load(item.book.photoUrl).into(dialogview.findViewById<ImageView>(R.id.bookMainImg))
            dialogview.findViewById<TextView>(R.id.bookName).text = "${item.book.name}"
            var authors: String? = ""
            item.book.authors.forEach {
                authors += "$it, "
            }
            var cats: String? = ""
            item.book.categories.forEach {
                cats += "$it, "
            }
            dialogview.findViewById<TextView>(R.id.bookAuthors).text = "$authors"
            dialogview.findViewById<TextView>(R.id.bookPublishers).text = "${item.book.publisher}"
            dialogview.findViewById<TextView>(R.id.bookCategories).text = "$cats"
            dialogview.findViewById<TextView>(R.id.bookStock).text = "${item.book.amountInStock}"

            dialogview.findViewById<Button>(R.id.inc_button).setOnClickListener {
                var q = dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt()
                if ( q < item.book.amountInStock!! )
                {
                    q++
                    dialogview.findViewById<TextView>(R.id.quantity_text).text = q.toString()
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
                    dialogview.findViewById<TextView>(R.id.quantity_text).text = q.toString()
                }
                else
                {
                    Toast.makeText(context, "Quantity has to be greater than 0", Toast.LENGTH_SHORT).show()
                }
            }

            dialogview.findViewById<Button>(R.id.addToCart_button).setOnClickListener {
                ch.addToCartBook(item.book, dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt(), FirebaseAuth.getInstance().uid!!)
            }
            dialog.setView(dialogview)
        }
        else if ( item is EssentialAdapter )
        {
            layoutInflater = LayoutInflater.from(context)
            dialog = AlertDialog.Builder(context).create()
            val dialogview = layoutInflater.inflate(R.layout.essential_display, null)
            Picasso.get().load(item.essential.photoUrl).into(dialogview.findViewById<ImageView>(R.id.essentialMainImg))
            dialogview.findViewById<TextView>(R.id.essentialName).text = "${item.essential.name}"
            dialogview.findViewById<TextView>(R.id.essentialStock).text = "${item.essential.amountInStock.toString()}"
            dialogview.findViewById<TextView>(R.id.essentialManufacturer).text = "${item.essential.manufacturer}"

            dialogview.findViewById<Button>(R.id.inc_button).setOnClickListener {
                var q = dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt()
                if ( q < item.essential.amountInStock!! )
                {
                    q++
                    dialogview.findViewById<TextView>(R.id.quantity_text).text = q.toString()
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
                    dialogview.findViewById<TextView>(R.id.quantity_text).text = q.toString()
                }
                else
                {
                    Toast.makeText(context, "Quantity has to be greater than 0", Toast.LENGTH_SHORT).show()
                }
            }

            dialogview.findViewById<Button>(R.id.addToCart_button).setOnClickListener {
                ch.addToCartEssential(item.essential, dialogview.findViewById<TextView>(R.id.quantity_text).text.toString().toInt(), FirebaseAuth.getInstance().uid!!)
            }
            dialog.setView(dialogview)
        }
        dialog.setCancelable(true)
        dialog.show()
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

    override fun quantityFailure() {
        Toast.makeText(context, "Please enter a quantity greater than 0", Toast.LENGTH_SHORT).show()
    }

    interface onExitListener
    {
        fun onExit()
    }

    fun setOnExitListener(lol: onExitListener)
    {
        this.mOnExitListener = lol
    }
}