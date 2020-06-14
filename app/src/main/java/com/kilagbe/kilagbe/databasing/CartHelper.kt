package com.kilagbe.kilagbe.databasing


import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.data.*
import java.text.SimpleDateFormat
import java.util.*

class CartHelper(var context: Context?) : ItemHelper.changeAmountEssentialSuccessListener, ItemHelper.changeAmountBookSuccessListener, ItemHelper.changeAmountFailureListener {

    constructor():this(null)
    val ih = ItemHelper()
    init {
        ih.setChangeAmountBookSuccessListener(this)
        ih.setChangeAmountEssentialSuccessListener(this)
    }

    private lateinit var mCartFoundListener: cartFoundListener
    private lateinit var mUpdateCartSuccessListener: updateCartSuccessListener
    private lateinit var mUpdateCartFailureListener: updateCartFailureListener
    private lateinit var mCartNotFoundFailureListener: cartNotFoundFailureListener
    private lateinit var mQuantityFailureListener: quantityFailureListener
    private lateinit var mCheckoutSuccessListener: checkoutSuccessListener
    private lateinit var mCheckoutFailureListener: checkoutFailureListener

    //cart functionality
    fun addToCartBook(book: Book, qty: Int, uid: String)
    {
        //check if qty > 0 or not
        if ( qty > 0 )
        {
            var cart: Cart

            //make order item
            val order = OrderItems(book.itemId!!)
            order.qty = qty
            order.cost = book.price * qty

            //check whether cart exists or not
            val dbref = FirebaseFirestore.getInstance().collection("carts").document(uid)
            dbref.get()
                .addOnSuccessListener {
                    //if it does, add to it
                    if ( it.exists() )
                    {
                        cart = it.toObject(Cart::class.java)!!
                        //if item is already added in cart, increment it
                        val oldList = cart.orderBookItems
                        if ( (oldList.filter { it.itemid == book.itemId }).isNotEmpty() )
                        {
                            //get item index and update at index
                            val ind = oldList.indexOfFirst { it.itemid == book.itemId }
                            oldList[ind].qty = oldList[ind].qty?.plus(qty)
                            oldList[ind].cost = oldList[ind].cost?.plus(order.cost!!)
                        }
                        else
                        {
                            oldList.add(order)
                        }
                        cart.orderBookItems = oldList
                        cart.total = cart.total?.plus(order.cost!!)
                    }
                    else
                    //else create new cart
                    {
                        //make new cart
                        cart = Cart(uid)
                        cart.orderBookItems.add(order)
                        cart.status = "ORDERING"
                        cart.total = cart.total?.plus(order.cost!!)
                    }
                    //update cart in database
                    dbref.set(cart)
                        .addOnSuccessListener {
                            //update essential item in database
                            ih.changeAmountBook(book.itemId!!, (-1 * qty))
                            mUpdateCartSuccessListener.updateCartSuccess()
                        }
                        .addOnFailureListener {
                            mUpdateCartFailureListener.updateCartFailure()
                        }
                }
                .addOnFailureListener {
                    //failure to fetch document because it doesn't exist
                    mCartNotFoundFailureListener.cartNotFoundFailure()
                }
        }
        else
        //if not qty > 0, don't add
        {
            mQuantityFailureListener.quantityFailure()
        }
    }

    fun addToCartEssential(essential: Essential, qty: Int, uid: String)
    {
        //check if qty > 0 or not
        if ( qty > 0 )
        {
            var cart: Cart

            //make order item
            val order = OrderItems(essential.itemId!!)
            order.qty = qty
            order.cost = essential.price * qty

            //check whether cart exists or not
            val dbref = FirebaseFirestore.getInstance().collection("carts").document(uid)
            dbref.get()
                .addOnSuccessListener {
                    //if it does, add to it
                    if ( it.exists() )
                    {
                        cart = it.toObject(Cart::class.java)!!
                        //if item is already added in cart, increment it
                        val oldList = cart.orderEssentialItems
                        if ( (oldList.filter { it.itemid == essential.itemId }).isNotEmpty() )
                        {
                            //get item index and update at index
                            val ind = oldList.indexOfFirst { it.itemid == essential.itemId }
                            oldList[ind].qty = oldList[ind].qty?.plus(qty)
                            oldList[ind].cost = oldList[ind].cost?.plus(order.cost!!)
                        }
                        else
                        {
                            oldList.add(order)
                        }
                        cart.orderEssentialItems = oldList
                        cart.total = cart.total?.plus(order.cost!!)
                    }
                    else
                    //else create new cart
                    {
                        //make new cart
                        cart = Cart(uid)
                        cart.orderEssentialItems.add(order)
                        cart.status = "ORDERING"
                        cart.total = cart.total?.plus(order.cost!!)
                    }
                    //update cart in database
                    dbref.set(cart)
                        .addOnSuccessListener {
                            //update essential item in database
                            ih.changeAmountEssential(essential.itemId!!, (-1 * qty))
                            mUpdateCartSuccessListener.updateCartSuccess()
                        }
                        .addOnFailureListener {
                            mUpdateCartFailureListener.updateCartFailure()
                        }
                }
                .addOnFailureListener {
                    //failure to fetch document because it doesn't exist
                    mCartNotFoundFailureListener.cartNotFoundFailure()
                }
        }
        else
        //if not qty > 0, don't add
        {
            mQuantityFailureListener.quantityFailure()
        }
    }

    fun modifyCartBook(bookId: String, qty: Int, uid: String)
    {
        val dbref = FirebaseFirestore.getInstance().collection("carts").document(uid)
        dbref.get()
            .addOnSuccessListener {
                if ( it.exists() )
                {
                    val temp = it.toObject(Cart::class.java)
                    val oldList = temp!!.orderBookItems
                    if ( (oldList.filter { it.itemid == bookId }).isNotEmpty() )
                    //item already exists in cart
                    {
                        val ind = oldList.indexOfFirst { it.itemid == bookId }

                        //cost calculations
                        val addCost = oldList[ind].cost?.div(oldList[ind].qty!!)?.times(qty)
                        val subtractCost = oldList[ind].cost
                        val incCost = addCost?.minus(subtractCost!!)
                        oldList[ind].cost = addCost

                        //qty calculations
                        val newQty = qty
                        val oldQty = oldList[ind].qty
                        val incQty = newQty.minus(oldQty!!)
                        oldList[ind].qty = qty

                        //update array in temp
                        temp.orderBookItems = oldList
                        temp.total = temp.total?.plus(incCost!!)

                        //update cart in database
                        dbref.set(temp)
                            .addOnSuccessListener {
                                //update item in database
                                ih.changeAmountBook(bookId, (-1 * incQty))
                                mUpdateCartSuccessListener.updateCartSuccess()
                            }
                            .addOnFailureListener {
                                mUpdateCartFailureListener.updateCartFailure()
                            }
                    }
                    else
                    //item does not exist in cart
                    {
                        mUpdateCartFailureListener.updateCartFailure()
                    }
                }
                else
                {
                    mCartNotFoundFailureListener.cartNotFoundFailure()
                }
            }
            .addOnFailureListener {
                mCartNotFoundFailureListener.cartNotFoundFailure()
            }
    }

    fun modifyCartEssential(essentialId: String, qty: Int, uid: String)
    {
        val dbref = FirebaseFirestore.getInstance().collection("carts").document(uid)
        dbref.get()
            .addOnSuccessListener {
                if ( it.exists() )
                {
                    val temp = it.toObject(Cart::class.java)
                    val oldList = temp!!.orderEssentialItems
                    if ( (oldList.filter { it.itemid == essentialId }).isNotEmpty() )
                    //item already exists in cart
                    {
                        val ind = oldList.indexOfFirst { it.itemid == essentialId }

                        //cost calculations
                        val addCost = oldList[ind].cost?.div(oldList[ind].qty!!)?.times(qty)
                        val subtractCost = oldList[ind].cost
                        val incCost = addCost?.minus(subtractCost!!)
                        oldList[ind].cost = addCost

                        //qty calculations
                        val newQty = qty
                        val oldQty = oldList[ind].qty
                        val incQty = newQty.minus(oldQty!!)
                        oldList[ind].qty = qty

                        //update array in temp
                        temp.orderEssentialItems = oldList
                        temp.total = temp.total?.plus(incCost!!)

                        //update cart in database
                        dbref.set(temp)
                            .addOnSuccessListener {
                                //update item in database
                                ih.changeAmountEssential(essentialId, (-1 * incQty))
                                mUpdateCartSuccessListener.updateCartSuccess()
                            }
                            .addOnFailureListener {
                                mUpdateCartFailureListener.updateCartFailure()
                            }
                    }
                    else
                    //item does not exist in cart
                    {
                        mUpdateCartFailureListener.updateCartFailure()
                    }
                }
                else
                {
                    mCartNotFoundFailureListener.cartNotFoundFailure()
                }
            }
            .addOnFailureListener {
                mCartNotFoundFailureListener.cartNotFoundFailure()
            }
    }

    fun deleteCartBook(bookId: String, uid: String)
    {
        val dbref = FirebaseFirestore.getInstance().collection("carts").document(uid)
        dbref.get()
            .addOnSuccessListener {
                if ( it.exists() )
                {
                    val temp = it.toObject(Cart::class.java)
                    val oldList = temp!!.orderBookItems
                    if ( (oldList.filter { it.itemid == bookId }).isNotEmpty() )
                    //item already exists in cart
                    {
                        val ind = oldList.indexOfFirst { it.itemid == bookId }

                        //cost calculations
                        val decCost = oldList[ind].cost

                        //qty calculation
                        val incQty = oldList[ind].qty

                        //remove item from list
                        oldList.removeAt(ind)

                        //update array in temp
                        temp.orderBookItems = oldList
                        temp.total = temp.total?.minus(decCost!!)

                        //update cart in database
                        dbref.set(temp)
                            .addOnSuccessListener {
                                //update item in database
                                ih.changeAmountBook(bookId, incQty!!)
                                mUpdateCartSuccessListener.updateCartSuccess()
                            }
                            .addOnFailureListener {
                                mUpdateCartFailureListener.updateCartFailure()
                            }
                    }
                    else
                    //item does not exist in cart
                    {
                        mUpdateCartFailureListener.updateCartFailure()
                    }
                }
                else
                {
                    mCartNotFoundFailureListener.cartNotFoundFailure()
                }
            }
            .addOnFailureListener {
                mCartNotFoundFailureListener.cartNotFoundFailure()
            }
    }

    fun deleteCartEssential(essentialId: String, uid: String)
    {
        val dbref = FirebaseFirestore.getInstance().collection("carts").document(uid)
        dbref.get()
            .addOnSuccessListener {
                if ( it.exists() )
                {
                    val temp = it.toObject(Cart::class.java)
                    val oldList = temp!!.orderEssentialItems
                    if ( (oldList.filter { it.itemid == essentialId }).isNotEmpty() )
                    //item already exists in cart
                    {
                        val ind = oldList.indexOfFirst { it.itemid == essentialId }

                        //cost calculations
                        val decCost = oldList[ind].cost

                        //qty calculation
                        val incQty = oldList[ind].qty

                        //remove item from list
                        oldList.removeAt(ind)

                        //update array in temp
                        temp.orderBookItems = oldList
                        temp.total = temp.total?.minus(decCost!!)

                        //update cart in database
                        dbref.set(temp)
                            .addOnSuccessListener {
                                //update item in database
                                ih.changeAmountEssential(essentialId, incQty!!)
                                mUpdateCartSuccessListener.updateCartSuccess()
                            }
                            .addOnFailureListener {
                                mUpdateCartFailureListener.updateCartFailure()
                            }
                    }
                    else
                    //item does not exist in cart
                    {
                        mUpdateCartFailureListener.updateCartFailure()
                    }
                }
                else
                {
                    mCartNotFoundFailureListener.cartNotFoundFailure()
                }
            }
            .addOnFailureListener {
                mCartNotFoundFailureListener.cartNotFoundFailure()
            }
    }

    fun fetchCart(uid: String)
    {
        FirebaseFirestore.getInstance().collection("carts").document(uid).get()
            .addOnSuccessListener {
                if ( it.exists() )
                {
                    val temp = it.toObject(Cart::class.java)
                    mCartFoundListener.cartFound(temp!!)
                }
                else
                {
                    mCartNotFoundFailureListener.cartNotFoundFailure()
                }
            }
            .addOnFailureListener {
                mCartNotFoundFailureListener.cartNotFoundFailure()
            }
    }

    fun checkoutCart(uid: String, address: String, charge: Double)
    {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd__HH-mm-ss")
//        val _timestamp = Date().date.toString() + Date().month.toString() + Date().year.toString() + "-"  + Date().hours.toString() + Date().minutes.toString() + Date().seconds.toString()
        val _timestamp = formatter.format(time)
        val order = CompleteOrder(uid, _timestamp)
        val cartRef = FirebaseFirestore.getInstance().collection("carts").document(uid)
        cartRef.get()
            .addOnSuccessListener {
                //check to see if cart exists and complete order accordingly
                if ( it.exists() )
                {
                    val temp = it.toObject(Cart::class.java)
                    FirebaseFirestore.getInstance().collection("customer").document(uid).get()
                        .addOnSuccessListener {
                            if ( it.exists() )
                            {
                                val cust = it.toObject(User::class.java)
                                order.customerstatus = "AWAITING DELIVERY"
                                order.deliverymanstatus = "AWAITING PICK UP"
                                order.deliverymanphone = ""
                                order.orderId = order.customeruid + "-" + order.timestamp
                                temp!!.total = temp.total!!.plus(charge)
                                order.cart = temp
                                order.address = address
                                order.customerphone = cust!!.phone
                                val orderref = FirebaseFirestore.getInstance().collection("orders").document(order.orderId!!)
                                orderref.set(order)
                                    .addOnSuccessListener {
                                        //reset cart
                                        cartRef.delete()
                                        mCheckoutSuccessListener.checkoutSuccess()
                                    }
                                    .addOnFailureListener {
                                        mCheckoutFailureListener.checkoutFailure()
                                    }
                            }
                            else
                            {
                                mCheckoutFailureListener.checkoutFailure()
                            }
                        }
                        .addOnFailureListener {
                            mCheckoutFailureListener.checkoutFailure()
                        }
                }
                else
                //if no cart found, nothing to check out, so error
                {
                    mCartNotFoundFailureListener.cartNotFoundFailure()
                }
            }
            .addOnFailureListener {
                //error fetching cart
                mCartNotFoundFailureListener.cartNotFoundFailure()
            }
    }

    override fun changeAmountBookSuccess() {
        Toast.makeText(context, "Updated book in database", Toast.LENGTH_SHORT).show()
    }

    override fun changeAmountEssentialSuccess() {
        Toast.makeText(context, "Updated essential in database", Toast.LENGTH_SHORT).show()
    }

    //utility functions
    fun setUpdateCartSuccessListener(lol: updateCartSuccessListener)
    {
        this.mUpdateCartSuccessListener = lol
    }

    fun setUpdateCartFailureListener(lol: updateCartFailureListener)
    {
        this.mUpdateCartFailureListener = lol
    }

    fun setCartNotFoundFailureListener(lol: cartNotFoundFailureListener)
    {
        this.mCartNotFoundFailureListener = lol
    }

    fun setCartFoundListener(lol: cartFoundListener)
    {
        this.mCartFoundListener = lol
    }

    fun setQuantityFailureListener(lol: quantityFailureListener)
    {
        this.mQuantityFailureListener = lol
    }

    fun setCheckoutSuccessListener(lol: checkoutSuccessListener)
    {
        this.mCheckoutSuccessListener = lol
    }

    fun setCheckoutFailureListener(lol: checkoutFailureListener)
    {
        this.mCheckoutFailureListener = lol
    }

    //interfaces
    interface updateCartSuccessListener
    {
        fun updateCartSuccess()
    }

    interface updateCartFailureListener
    {
        fun updateCartFailure()
    }

    interface cartNotFoundFailureListener
    {
        fun cartNotFoundFailure()
    }

    interface cartFoundListener
    {
        fun cartFound(cart: Cart)
    }

    interface quantityFailureListener
    {
        fun quantityFailure()
    }

    interface checkoutSuccessListener
    {
        fun checkoutSuccess()
    }

    interface checkoutFailureListener
    {
        fun checkoutFailure()
    }

    override fun changeAmountFailure() {
        Toast.makeText(context, "Failed to change amount", Toast.LENGTH_SHORT).show()
    }
}