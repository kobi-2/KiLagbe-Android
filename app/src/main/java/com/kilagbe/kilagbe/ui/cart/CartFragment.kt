package com.kilagbe.kilagbe.ui.cart

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Cart
import com.kilagbe.kilagbe.data.OrderItems
import com.kilagbe.kilagbe.tools.OrderItemAdapter
import com.kilagbe.kilagbe.tools.OrderItemOnClickListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : Fragment(), OrderItemOnClickListener.onExitListener {

    lateinit var cartrecycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cart, container, false)

        cartrecycler = root.findViewById<RecyclerView>(R.id.orderItemsRecycler)

        root.findViewById<Button>(R.id.checkout_button).setOnClickListener {
            FirebaseFirestore.getInstance().collection("carts").document(FirebaseAuth.getInstance().uid.toString()).update("status", "PENDING")
                .addOnSuccessListener {
                    Toast.makeText(activity, "Checked out successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        return root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        initRecyclerView(this!!.activity!!)
        super.onStart()
    }

    fun initRecyclerView(context: Context) {
        val adapter = GroupAdapter<GroupieViewHolder>()

        FirebaseFirestore.getInstance().collection("carts").document(FirebaseAuth.getInstance().uid.toString()).get()
            .addOnSuccessListener {
                if ( it!!.exists() )
                {
                    val temp = it.toObject(Cart::class.java)
                    if ( temp!!.orderItems.isNotEmpty() )
                    {
                        temp!!.orderItems.forEach { orderItem ->
                            adapter.add(OrderItemAdapter(orderItem))
                        }
                    }
                    else
                    {
                        Toast.makeText(context, "No items in cart", Toast.LENGTH_SHORT).show()
                    }
                    val listener = OrderItemOnClickListener(context)
                    listener.setOnExitListener(this)
                    adapter.setOnItemClickListener(listener)
                    cartrecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
                    cartrecycler.adapter = adapter
                }
                else
                {
                    Toast.makeText(activity, "No cart found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onExit() {
        Log.d("ONEXIT", "Interface call from cart fragment")
        initRecyclerView(this!!.activity!!)
    }
}
