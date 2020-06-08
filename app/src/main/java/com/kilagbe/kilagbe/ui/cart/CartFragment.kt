package com.kilagbe.kilagbe.ui.cart

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Cart
import com.kilagbe.kilagbe.databasing.CartHelper
import com.kilagbe.kilagbe.tools.CustomerOrderAdapter
import com.kilagbe.kilagbe.tools.OrderItemOnClickListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class CartFragment : Fragment(), OrderItemOnClickListener.onExitListener, CartHelper.cartFoundListener, CartHelper.cartNotFoundFailureListener, CartHelper.checkoutSuccessListener, CartHelper.checkoutFailureListener {

    lateinit var ch: CartHelper

    lateinit var addressDialog: AlertDialog
    lateinit var mUserAddress: String

    lateinit var cartrecycler: RecyclerView
    lateinit var totalText: TextView
    lateinit var adapter: GroupAdapter<GroupieViewHolder>

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cart, container, false)

        ch = CartHelper(this!!.activity!!)

        ch.setCartFoundListener(this)
        ch.setCartNotFoundFailureListener(this)
        ch.setCheckoutSuccessListener(this)
        ch.setCheckoutFailureListener(this)

        cartrecycler = root.findViewById<RecyclerView>(R.id.orderItemsRecycler)
        totalText = root.findViewById<TextView>(R.id.cart_total)
        root.findViewById<Button>(R.id.checkout_button).setOnClickListener {

            addressDialog = AlertDialog.Builder(context).create()
            val addressDialogview = layoutInflater.inflate(R.layout.alert_dialog_user_confirm_address, null)

            addressDialogview.findViewById<Button>(R.id.confirm_address_button).setOnClickListener {

                val editText = addressDialogview.findViewById(R.id.user_address_text) as EditText
                mUserAddress = editText.text.toString()

                if(mUserAddress.isNullOrBlank()){
                    Toast.makeText(context, "Please Provide An Address", Toast.LENGTH_SHORT).show()
                }
                else{

                    val mAlertDialog = AlertDialog.Builder(context).create()
                    mAlertDialog.setTitle("Confirm Address")
                    mAlertDialog.setMessage("Do You Confirm This Address?")
                    mAlertDialog.setButton(Dialog.BUTTON_POSITIVE, "YES", DialogInterface.OnClickListener { dialog, which ->

//                          todo: not a todo, but rather a note --- checkoutCart is now here...
                        ch.checkoutCart(FirebaseAuth.getInstance().uid.toString(), mUserAddress)
                        Toast.makeText(context, "Address Confirmed", Toast.LENGTH_SHORT).show()
                        addressDialog.dismiss()
                    })

                    mAlertDialog.setButton(Dialog.BUTTON_NEGATIVE, "NO") { dialog, which ->
                        Toast.makeText(context, "Address Cancelled", Toast.LENGTH_SHORT).show()
                    }

                    mAlertDialog.show()
                }

            }

            addressDialog.setView(addressDialogview)
//            todo: should there be " addressDialog.setCancelable(true) " ?
//            addressDialog.setCancelable(true)
            addressDialog.show()

        }

        return root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        initRecyclerView(this!!.activity!!)
        super.onStart()
    }

    fun initRecyclerView(context: Context) {
        adapter = GroupAdapter<GroupieViewHolder>()

        ch.fetchCart(FirebaseAuth.getInstance().uid.toString())
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onExit() {
        Log.d("ONEXIT", "Interface call from cart fragment")
        initRecyclerView(this!!.activity!!)
    }

    override fun cartNotFoundFailure() {
        cartrecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
        cartrecycler.adapter = adapter
        totalText.text = "0"
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun cartFound(cart: Cart) {
        val context = this!!.activity!!
        if ( cart.orderBookItems.isNotEmpty() )
        {
            cart.orderBookItems.forEach { orderItem ->
                adapter.add(CustomerOrderAdapter(orderItem, context))
            }
        }
        else
        {
            Toast.makeText(context, "No book items in cart", Toast.LENGTH_SHORT).show()
        }

        if ( cart.orderEssentialItems.isNotEmpty() )
        {
            cart.orderEssentialItems.forEach { orderItem ->
                adapter.add(CustomerOrderAdapter(orderItem, context))
            }
        }
        else
        {
            Toast.makeText(context, "No essential items in cart", Toast.LENGTH_SHORT).show()
        }
        val listener = OrderItemOnClickListener(context)
        listener.setOnExitListener(this)
        adapter.setOnItemClickListener(listener)
        cartrecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
        cartrecycler.adapter = adapter
        totalText.text = (cart.total.toString())
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun checkoutSuccess() {
        Toast.makeText(this.activity, "Added cart to orders", Toast.LENGTH_SHORT).show()
        initRecyclerView(this!!.activity!!)
    }

    override fun checkoutFailure() {
        Toast.makeText(this.activity, "Failed to check out", Toast.LENGTH_SHORT).show()
    }
}
