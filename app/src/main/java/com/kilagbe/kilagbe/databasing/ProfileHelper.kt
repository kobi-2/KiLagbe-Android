package com.kilagbe.kilagbe.databasing

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.data.User

class ProfileHelper {

    lateinit var mGetCustomerSuccessListener: getCustomerSuccessListener
    lateinit var mGetCustomerFailureListener: getCustomerFailureListener

    lateinit var mGetDeliverymanSuccessListener: getDeliverymanSuccessListener
    lateinit var mGetDeliverymanFailureListener: getDeliverymanFailureListener

    //profile functionality
    fun getUid(): String?
    {
        if ( FirebaseAuth.getInstance().currentUser != null )
        {
            return FirebaseAuth.getInstance().currentUser!!.uid.toString()
        }
        else
        {
            return null
        }
    }

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

    fun getDeliveryman(uid: String)
    {
        FirebaseFirestore.getInstance().collection("deliveryman").document(uid).get()
            .addOnSuccessListener {
                if ( it.exists() )
                {
                    val temp = it.toObject(User::class.java)
                    mGetDeliverymanSuccessListener.getDeliverymanSuccess(temp!!)
                }
                else
                {
                    mGetDeliverymanFailureListener.getDeliverymanFailure()
                }
            }
            .addOnFailureListener {
                mGetDeliverymanFailureListener.getDeliverymanFailure()
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

    fun setGetDeliverymanSuccessListener(lol: getDeliverymanSuccessListener)
    {
        this.mGetDeliverymanSuccessListener = lol
    }

    fun setGetDeliverymanFailureListener(lol: getDeliverymanFailureListener)
    {
        this.mGetDeliverymanFailureListener = lol
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

    interface getDeliverymanFailureListener {
        fun getDeliverymanFailure()
    }

    interface getDeliverymanSuccessListener {
        fun getDeliverymanSuccess(deliveryman: User)
    }
}


