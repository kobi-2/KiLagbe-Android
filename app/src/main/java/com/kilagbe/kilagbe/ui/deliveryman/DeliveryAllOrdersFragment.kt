package com.kilagbe.kilagbe.ui.deliveryman

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Cart
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

/**
 * A simple [Fragment] subclass.
 */
class DeliveryAllOrdersFragment : Fragment() {

    private lateinit var allOrdersRecycler: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_delivery_all_orders, container, false)
        allOrdersRecycler = root.findViewById(R.id.all_orders_recycler_view)
        return root
    }

//    private fun initRecyclerView(context: Context){
//
//        val allOrdersAdapter = GroupAdapter<GroupieViewHolder>()
//
//        FirebaseFirestore.getInstance().collection("carts").whereEqualTo("status", "PENDING").get()
//            .addOnSuccessListener {
//                if ( it.isEmpty )
//                {
//                    Toast.makeText(context, "No pending orders", Toast.LENGTH_SHORT).show()
//                }
//                else
//                {
//                    for ( doc in it.documents )
//                    {
//                        val temp = doc.toObject(Cart::class.java)
//
//                    }
//                }
//            }
//    }
}
