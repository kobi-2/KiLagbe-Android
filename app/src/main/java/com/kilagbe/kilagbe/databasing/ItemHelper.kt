package com.kilagbe.kilagbe.databasing


import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.data.Book
import com.kilagbe.kilagbe.data.Essential

class ItemHelper(val context: Context) {

    private lateinit var mGetBookSuccessListener: getBookSuccessListener
    private lateinit var mGetBookFailureListener: getBookFailureListener

    private lateinit var mChangeAmountBookSuccessListener: changeAmountBookSuccessListener

    private lateinit var mGetEssentialSuccessListener: getEssentialSuccessListener
    private lateinit var mGetEssentialFailureListener: getEssentialFailureListener

    private lateinit var mChangeAmountEssentialSuccessListener: changeAmountEssentialSuccessListener


    //item functionality - will be further refactored
    fun getBook(itemId: String)
    {
        FirebaseFirestore.getInstance().collection("books").document(itemId).get()
            .addOnSuccessListener {
                if ( it.exists() )
                {
                    val temp = it.toObject(Book::class.java)
                    mGetBookSuccessListener.getBookSuccess(temp!!)
                }
                else
                {
                    mGetBookFailureListener.getBookFailure()
                }
            }
            .addOnFailureListener {
                mGetBookFailureListener.getBookFailure()
            }
    }

    fun getEssential(itemId: String)
    {
        FirebaseFirestore.getInstance().collection("essentials").document(itemId).get()
            .addOnSuccessListener {
                if ( it.exists() )
                {
                    val temp = it.toObject(Essential::class.java)
                    mGetEssentialSuccessListener.getEssentialSuccess(temp!!)
                }
                else
                {
                    mGetEssentialFailureListener.getEssentialFailure()
                }
            }
            .addOnFailureListener {
                mGetEssentialFailureListener.getEssentialFailure()
            }
    }

    fun changeAmountBook(itemid: String, inc: Int)
    {
        FirebaseFirestore.getInstance().collection("books").document(itemid).update("amountInStock", FieldValue.increment(inc.toLong()))
            .addOnSuccessListener {
                mChangeAmountBookSuccessListener.changeAmountBookSuccess()
            }
            .addOnFailureListener {
                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun changeAmountEssential(itemid: String, inc: Int)
    {
        FirebaseFirestore.getInstance().collection("essentials").document(itemid).update("amountInStock", FieldValue.increment(inc.toLong()))
            .addOnSuccessListener {
                mChangeAmountEssentialSuccessListener.changeAmountEssentialSuccess()
            }
            .addOnFailureListener {
                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    //utility functions
    fun setGetBookSuccessListener(lol: getBookSuccessListener)
    {
        this.mGetBookSuccessListener = lol
    }

    fun setGetBookFailureListener(lol: getBookFailureListener)
    {
        this.mGetBookFailureListener = lol
    }

    fun setGetEssentialSuccessListener(lol: getEssentialSuccessListener)
    {
        this.mGetEssentialSuccessListener = lol
    }

    fun setGetEssentialFailureListener(lol: getEssentialFailureListener)
    {
        this.mGetEssentialFailureListener = lol
    }

    fun setChangeAmountBookSuccessListener(lol: changeAmountBookSuccessListener)
    {
        this.mChangeAmountBookSuccessListener = lol
    }

    fun setChangeAmountEssentialSuccessListener(lol: changeAmountEssentialSuccessListener)
    {
        this.mChangeAmountEssentialSuccessListener = lol
    }

    //interfaces
    interface getBookSuccessListener
    {
        fun getBookSuccess(book: Book)
    }

    interface getBookFailureListener
    {
        fun getBookFailure()
    }

    interface getEssentialSuccessListener
    {
        fun getEssentialSuccess(essential: Essential)
    }

    interface getEssentialFailureListener
    {
        fun getEssentialFailure()
    }

    interface changeAmountBookSuccessListener
    {
        fun changeAmountBookSuccess()
    }

    interface changeAmountEssentialSuccessListener
    {
        fun changeAmountEssentialSuccess()
    }
}