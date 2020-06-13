package com.kilagbe.kilagbe.databasing

import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.data.CompleteOrder
import com.kilagbe.kilagbe.data.User

class OrderHelper {
    private lateinit var mGetOrdersSuccessListener: getOrdersSuccessListener
    private lateinit var mGetOrdersFailureListener: getOrdersFailureListener
    private lateinit var mConfirmOrderSuccessListener: confirmOrderSuccessListener
    private lateinit var mConfirmOrderFailureListener: confirmOrderFailureListener

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

    fun getDeliverymanPersonalOrders(uid: String)
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

    fun acceptOrder(orderid: String, deliverymanuid: String)
    {
        val dbref = FirebaseFirestore.getInstance().collection("orders").document(orderid)
        dbref.get()
            .addOnSuccessListener {
                val temp = it.toObject(CompleteOrder::class.java)
                FirebaseFirestore.getInstance().collection("deliveryman").document(deliverymanuid).get()
                    .addOnSuccessListener {
                        val deliveryman = it.toObject(User::class.java)
                        temp!!.deliverymanuid = deliverymanuid
                        temp.deliverymanphone = deliveryman!!.phone
                        temp.deliverymanstatus = "PICKED UP"
                        dbref.set(temp)
                            .addOnSuccessListener {
                                mConfirmOrderSuccessListener.confirmOrderSuccess()
                            }
                            .addOnFailureListener {
                                mConfirmOrderFailureListener.confirmOrderFailure()
                            }
                    }
                    .addOnFailureListener {
                        mConfirmOrderFailureListener.confirmOrderFailure()
                    }
            }
            .addOnFailureListener {
                mConfirmOrderFailureListener.confirmOrderFailure()
            }
    }

    fun getCustomerPersonalOrders(uid: String)
    {
        var orderArray = arrayListOf<CompleteOrder>()
        FirebaseFirestore.getInstance().collection("orders").whereEqualTo("customeruid", uid).get()
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

    fun setConfirmOrderSuccessListener(lol: confirmOrderSuccessListener)
    {
        this.mConfirmOrderSuccessListener = lol
    }

    fun setConfirmOrderFailureListener(lol: confirmOrderFailureListener)
    {
        this.mConfirmOrderFailureListener = lol
    }

    //interfaces
    interface getOrdersSuccessListener{
        fun getOrdersSuccess(orderArray: ArrayList<CompleteOrder>)
    }

    interface getOrdersFailureListener{
        fun getOrdersFailure()
    }

    interface confirmOrderSuccessListener
    {
        fun confirmOrderSuccess()
    }

    interface confirmOrderFailureListener
    {
        fun confirmOrderFailure()
    }
}