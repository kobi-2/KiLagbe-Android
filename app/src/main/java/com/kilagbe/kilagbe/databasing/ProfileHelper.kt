package com.kilagbe.kilagbe.databasing

import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.data.User

class ProfileHelper {

    lateinit var mGetCustomerSuccessListener: getCustomerSuccessListener
    lateinit var mGetCustomerFailureListener: getCustomerFailureListener

    //profile functionality
    fun getCustomer(uid: String)
    {
        FirebaseFirestore.getInstance().collection("customer").document(uid).get()
            .addOnSuccessListener {
                if ( it.exists() )
                {
                    val temp = it.toObject(User::class.java)
                    mGetCustomerSuccessListener.getCustomerSuccess(temp!!)
                }
                else
                {
                    mGetCustomerFailureListener.getCustomerFailure()
                }
            }
            .addOnFailureListener {
                mGetCustomerFailureListener.getCustomerFailure()
            }
    }

    //utility functions
    fun setGetCustomerSuccessListener(lol: getCustomerSuccessListener)
    {
       this.mGetCustomerSuccessListener = lol
    }

    fun setGetCustomerFailureListener(lol: getCustomerFailureListener)
    {
        this.mGetCustomerFailureListener = lol
    }

    //interfaces
    interface getCustomerSuccessListener
    {
        fun getCustomerSuccess(customer: User)
    }

    interface getCustomerFailureListener
    {
        fun getCustomerFailure()
    }
}