package com.kilagbe.kilagbe.databasing

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.data.User

class ProfileHelper {

    lateinit var mGetCustomerSuccessListener: getCustomerSuccessListener
    lateinit var mGetCustomerFailureListener: getCustomerFailureListener

    lateinit var mGetDeliverymanSuccessListener: getDeliverymanSuccessListener
    lateinit var mGetDeliverymanFailureListener: getDeliverymanFailureListener

    lateinit var mChangeProfileSuccessListener: changeProfileSuccessListener
    lateinit var mChangeProfileFailureListener: changeProfileFailureListener

    //profile functionality
    fun getUid(): String?
    {
        return if ( FirebaseAuth.getInstance().currentUser != null ) {
            FirebaseAuth.getInstance().currentUser!!.uid.toString()
        } else {
            null
        }
    }

    fun logout()
    {
        FirebaseAuth.getInstance().signOut()
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

    fun getUser() : FirebaseUser
    {
        return FirebaseAuth.getInstance().currentUser!!
    }


    fun changeProfileEmail(email: String, usertype: String) {
        val user = getUser()
        user.updateEmail(email)
            .addOnSuccessListener {
                val dbref = FirebaseFirestore.getInstance().collection(usertype).document(user.uid)
                dbref.get()
                    .addOnSuccessListener {
                        if ( it.exists() )
                        {
                            var temp = it.toObject(User::class.java)
                            temp!!.email = email
                            dbref.set(temp)
                                .addOnSuccessListener {
                                    mChangeProfileSuccessListener.changeProfileSuccess()
                                }
                                .addOnFailureListener {
                                    mChangeProfileFailureListener.changeProfileFailure()
                                }
                        }
                        else
                        {
                            mChangeProfileFailureListener.changeProfileFailure()
                        }
                    }
                    .addOnFailureListener {
                        mChangeProfileFailureListener.changeProfileFailure()
                    }
            }
            .addOnFailureListener {
                mChangeProfileFailureListener.changeProfileFailure()
            }
    }

    fun changeProfileName(username: String, usertype: String)
    {
        val user = getUser()
        val upds = UserProfileChangeRequest.Builder().setDisplayName(username).build()
        user.updateProfile(upds)
            .addOnSuccessListener {
                val dbref = FirebaseFirestore.getInstance().collection(usertype).document(user.uid)
                dbref.get()
                    .addOnSuccessListener {
                        if ( it.exists() )
                        {
                            var temp = it.toObject(User::class.java)
                            temp!!.name = username
                            dbref.set(temp)
                                .addOnSuccessListener {
                                    mChangeProfileSuccessListener.changeProfileSuccess()
                                }
                                .addOnFailureListener {
                                    mChangeProfileFailureListener.changeProfileFailure()
                                }
                        }
                        else
                        {
                            mChangeProfileFailureListener.changeProfileFailure()
                        }
                    }
                    .addOnFailureListener {
                        mChangeProfileFailureListener.changeProfileFailure()
                    }
            }
            .addOnFailureListener {
                mChangeProfileFailureListener.changeProfileFailure()
            }
    }

    fun changeProfilePassword(password: String)
    {
        val user = getUser()
        user.updatePassword(password)
            .addOnSuccessListener {
                mChangeProfileSuccessListener.changeProfileSuccess()
            }
            .addOnFailureListener {
                mChangeProfileFailureListener.changeProfileFailure()
            }
    }

    fun changeProfilePhone(phone: String, usertype: String)
    {
        val user = getUser()
        FirebaseFirestore.getInstance().collection(usertype).document(user.uid).update("phone", phone)
            .addOnSuccessListener {
                mChangeProfileSuccessListener.changeProfileSuccess()
            }
            .addOnFailureListener {
                mChangeProfileFailureListener.changeProfileFailure()
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

    fun setChangeProfileSuccessListener(lol: changeProfileSuccessListener)
    {
        this.mChangeProfileSuccessListener = lol
    }

    fun setChangeProfileFailureListener(lol: changeProfileFailureListener)
    {
        this.mChangeProfileFailureListener = lol
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

    interface changeProfileSuccessListener
    {
        fun changeProfileSuccess()
    }

    interface changeProfileFailureListener
    {
        fun changeProfileFailure()
    }
}


