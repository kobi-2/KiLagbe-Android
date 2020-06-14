package com.kilagbe.kilagbe.databasing

import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.data.CompleteOrder
import com.kilagbe.kilagbe.data.User

class OrderHelper {
    private lateinit var mGetOrdersSuccessListener: getOrdersSuccessListener
    private lateinit var mGetOrdersFailureListener: getOrdersFailureListener
    private lateinit var mConfirmOrderSuccessListener: confirmOrderSuccessListener
    private lateinit var mConfirmOrderFailureListener: confirmOrderFailureListener
    private lateinit var mReceiveOrderSuccessListener: receiveOrderSuccessListener
    private lateinit var mReceiveOrderFailureListener: receiveOrderFailureListener
    private lateinit var mDeliverOrderSuccessListener: deliverOrderSuccessListener
    private lateinit var mDeliverOrderFailureListener: deliverOrderFailureListener

    //order functionality
    fun getDeliverymanAllOrders()
    {
        var orderArray = arrayListOf<CompleteOrder>()
        FirebaseFirestore.getInstance().collection("orders").whereEqualTo("deliverymanstatus", "AWAITING PICK UP").get()
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
        val orderArray = arrayListOf<CompleteOrder>()
        FirebaseFirestore.getInstance().collection("orders").whereEqualTo("deliverymanuid", uid).whereEqualTo("deliverymanstatus", "PICKED UP").get()
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

    fun acceptDeliverymanOrder(orderid: String, deliverymanuid: String)
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

    fun deliverDeliverymanOrder(orderid: String)
    {
        val dbref = FirebaseFirestore.getInstance().collection("orders").document(orderid)
        dbref.get()
            .addOnSuccessListener {
                val temp = it.toObject(CompleteOrder::class.java)
                temp!!.deliverymanstatus = "DELIVERED"
                dbref.set(temp)
                    .addOnSuccessListener {
                        mDeliverOrderSuccessListener.deliverOrderSuccess()
                    }
                    .addOnFailureListener {
                        mDeliverOrderFailureListener.deliverOrderFailure()
                    }
            }
            .addOnFailureListener {
                mDeliverOrderFailureListener.deliverOrderFailure()
            }

    }

    fun receiveCustomerOrder(orderid: String)
    {
        val dbref = FirebaseFirestore.getInstance().collection("orders").document(orderid)
        dbref.get()
            .addOnSuccessListener {
                val temp = it.toObject(CompleteOrder::class.java)
                temp!!.customerstatus = "RECEIVED"
                dbref.set(temp)
                    .addOnSuccessListener {
                        mReceiveOrderSuccessListener.receiveOrderSuccess()
                    }
                    .addOnFailureListener {
                        mReceiveOrderFailureListener.receiveOrderFailure()
                    }
            }
            .addOnFailureListener {
                mReceiveOrderFailureListener.receiveOrderFailure()
            }
    }

    fun getCustomerPersonalPastOrders(uid: String)
    {
        val orderArray = arrayListOf<CompleteOrder>()
        FirebaseFirestore.getInstance().collection("orders").whereEqualTo("customeruid", uid).whereEqualTo("customerstatus", "RECEIVED").get()
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

    fun getCustomerPersonalCurrentOrders(uid: String)
    {
        var orderArray = arrayListOf<CompleteOrder>()
        FirebaseFirestore.getInstance().collection("orders").whereEqualTo("customeruid", uid).whereEqualTo("customerstatus", "AWAITING DELIVERY").get()
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

    fun setReceiveOrderSuccessListener(lol: receiveOrderSuccessListener)
    {
        this.mReceiveOrderSuccessListener = lol
    }

    fun setReceiveOrderFailureListener(lol: receiveOrderFailureListener)
    {
        this.mReceiveOrderFailureListener = lol
    }

    fun setDeliverOrderSuccessListener(lol: deliverOrderSuccessListener)
    {
        this.mDeliverOrderSuccessListener = lol
    }

    fun setDeliverOrderFailureListener(lol: deliverOrderFailureListener)
    {
        this.mDeliverOrderFailureListener = lol
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

    interface receiveOrderSuccessListener
    {
        fun receiveOrderSuccess()
    }

    interface receiveOrderFailureListener
    {
        fun receiveOrderFailure()
    }

    interface deliverOrderSuccessListener
    {
        fun deliverOrderSuccess()
    }

    interface deliverOrderFailureListener
    {
        fun deliverOrderFailure()
    }
}