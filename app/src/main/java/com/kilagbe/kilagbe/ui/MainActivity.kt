package com.kilagbe.kilagbe.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.User
import com.kilagbe.kilagbe.databasing.ProfileHelper
import com.kilagbe.kilagbe.ui.auth.LoginActivity

class MainActivity : AppCompatActivity(), ProfileHelper.getCustomerSuccessListener, ProfileHelper.getCustomerFailureListener, ProfileHelper.getDeliverymanSuccessListener, ProfileHelper.getDeliverymanFailureListener{

    lateinit var ph: ProfileHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ph = ProfileHelper()
        ph.setGetDeliverymanFailureListener(this)
        ph.setGetDeliverymanSuccessListener(this)
        ph.setGetCustomerFailureListener(this)
        ph.setGetCustomerSuccessListener(this)

        checkUser()
    }

    fun checkUser()
    {
        val uid = ph.getUid()
        if ( uid == null )
        {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.and(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        else
        {
            ph.getCustomer(uid!!)
            ph.getDeliveryman(uid!!)
        }
    }

    override fun getCustomerSuccess(customer: User) {
        val intent = Intent(this, CustomerHome::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.and(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun getCustomerFailure() {
        Log.d("MainActivity", "Not a customer")
    }

    override fun getDeliverymanFailure() {
        Log.d("MainActivity", "Not a deliveryman")
    }

    override fun getDeliverymanSuccess(deliveryman: User) {
        val intent = Intent(this, DeliverymanHome::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.and(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}