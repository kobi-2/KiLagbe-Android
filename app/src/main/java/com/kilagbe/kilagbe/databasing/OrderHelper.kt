package com.kilagbe.kilagbe.databasing

import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.data.CompleteOrder

class OrderHelper {
    private lateinit var mGetOrdersSuccessListener: getOrdersSuccessListener
    private lateinit var mGetOrdersFailureListener: getOrdersFailureListener

    //order functionality
    fun getAllOrders()
    {
        var orderArray = arrayListOf<CompleteOrder>()
        FirebaseFirestore.getInstance().collection("orders").get()
            .addOnSuccessListener {
                if ( !it.isEmpty )
                {
                    for ( doc in it!! )
                    {
                       orderArray.add(doc.toObject(CompleteOrder::class.java))
                    }
                    mGetOrdersSuccessListener.getOrdersSuccess(orderArray)
                }
                else
                {
                    mGetOrdersFailureListener.getOrdersFailure()
                }
            }
            .addOnFailureListener {
                mGetOrdersFailureListener.getOrdersFailure()
            }
    }

    fun getPersonalOrders(uid: String)
    {
        var orderArray = arrayListOf<CompleteOrder>()
        FirebaseFirestore.getInstance().collection("orders").whereEqualTo("deliverymanuid", uid).get()
            .addOnSuccessListener {
                if ( !it.isEmpty )
                {
                    for ( doc in it!! )
                    {
                        val temp = doc.toObject(CompleteOrder::class.java)
                        orderArray.add(temp)
                    }
                    mGetOrdersSuccessListener.getOrdersSuccess(orderArray)
                }
                else
                {
                    mGetOrdersFailureListener.getOrdersFailure()
                }
            }
            .addOnFailureListener {
                mGetOrdersFailureListener.getOrdersFailure()
            }
    }

    fun acceptOrder()
    {

    }

    fun viewOrder()
    {

    }


    //utility functions
    fun setGetOrdersSuccessListener(lol: getOrdersSuccessListener)
    {
        this.mGetOrdersSuccessListener = lol
    }

    fun setGetOrdersFailureListener(lol: getOrdersFailureListener)
    {
        this.mGetOrdersFailureListener = lol
    }


    //interfaces
    interface getOrdersSuccessListener{
        fun getOrdersSuccess(orderArray: ArrayList<CompleteOrder>)
    }

    interface getOrdersFailureListener{
        fun getOrdersFailure()
    }
}