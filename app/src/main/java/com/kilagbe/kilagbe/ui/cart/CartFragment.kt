package com.kilagbe.kilagbe.ui.cart

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class CartFragment : Fragment() {

    lateinit var cartrecycler: RecyclerView

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cart, container, false)

        cartrecycler = root.findViewById<RecyclerView>(R.id.orderItemsRecycler)

        initRecyclerView(this!!.activity!!)
        return root
    }

    fun initRecyclerView(context: Context) {
        val adapter = GroupAdapter<GroupieViewHolder>()

        FirebaseFirestore.getInstance().collection("carts").document(FirebaseAuth.getInstance().uid.toString()).get()
            .addOnSuccessListener {
                if ( it!!.exists() )
                {
                    val temp = it.toObject(Cart::class.java)
                    temp!!.orderItems.forEach { orderItem ->
                        adapter.add(OrderItemAdapter(orderItem))
                    }
                    val listener = OrderItemOnClickListener(context,layoutInflater)
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
}
