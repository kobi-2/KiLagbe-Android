package com.kilagbe.kilagbe.databasing


import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.data.Book
import com.kilagbe.kilagbe.data.Essential

class ItemHelper {

    private lateinit var mGetBookSuccessListener: getBookSuccessListener
    private lateinit var mGetBookFailureListener: getBookFailureListener

    private lateinit var mGetAllBooksSuccessListener: getAllBooksSuccessListener
    private lateinit var mGetAllBooksFailureListener: getAllBooksFailureListener

    private lateinit var mGetAllEssentialsSuccessListener: getAllEssentialsSuccessListener
    private lateinit var mGetAllEssentialsFailureListener: getAllEssentialsFailureListener

    private lateinit var mGetCategoryBookSuccessListener: getCategoryBookSuccessListener
    private lateinit var mGetCategoryBookFailureListener: getCategoryBookFailureListener

    private lateinit var mGetDoubleCategoryBookSuccessListener: getDoubleCategoryBookSuccessListener
    private lateinit var mGetDoubleCategoryBookFailureListener: getDoubleCategoryBookFailureListener

    private lateinit var mChangeAmountBookSuccessListener: changeAmountBookSuccessListener

    private lateinit var mGetEssentialSuccessListener: getEssentialSuccessListener
    private lateinit var mGetEssentialFailureListener: getEssentialFailureListener

    private lateinit var mChangeAmountEssentialSuccessListener: changeAmountEssentialSuccessListener

    private lateinit var mChangeAmountFailureListener: changeAmountFailureListener


    //item functionality - will be further refactored
    fun getBook(itemId: String)
    {
        FirebaseFirestore.getInstance().collection("books").document(itemId).get()
            .addOnSuccessListener {
                if ( it.exists() )
                {
                    val temp = it.toObject(Book::class.java)
                    mGetBookSuccessListener.getBookSuccess(temp!!)
                }
                else
                {
                    mGetBookFailureListener.getBookFailure()
                }
            }
            .addOnFailureListener {
                mGetBookFailureListener.getBookFailure()
            }
    }

    fun getEssential(itemId: String)
    {
        FirebaseFirestore.getInstance().collection("essentials").document(itemId).get()
            .addOnSuccessListener {
                if ( it.exists() )
                {
                    val temp = it.toObject(Essential::class.java)
                    mGetEssentialSuccessListener.getEssentialSuccess(temp!!)
                }
                else
                {
                    mGetEssentialFailureListener.getEssentialFailure()
                }
            }
            .addOnFailureListener {
                mGetEssentialFailureListener.getEssentialFailure()
            }
    }

    fun changeAmountBook(itemid: String, inc: Int)
    {
        FirebaseFirestore.getInstance().collection("books").document(itemid).update("amountInStock", FieldValue.increment(inc.toLong()))
            .addOnSuccessListener {
                mChangeAmountBookSuccessListener.changeAmountBookSuccess()
            }
            .addOnFailureListener {
                mChangeAmountFailureListener.changeAmountFailure()
            }
    }

    fun changeAmountEssential(itemid: String, inc: Int)
    {
        FirebaseFirestore.getInstance().collection("essentials").document(itemid).update("amountInStock", FieldValue.increment(inc.toLong()))
            .addOnSuccessListener {
                mChangeAmountEssentialSuccessListener.changeAmountEssentialSuccess()
            }
            .addOnFailureListener {
                mChangeAmountFailureListener.changeAmountFailure()
            }
    }

    fun getCategoryBook(category: String)
    {
        FirebaseFirestore.getInstance().collection("books").whereArrayContains("categories", category).get()
            .addOnSuccessListener {
                if ( !it.isEmpty )
                {
                    var tempArray = arrayListOf<Book>()
                    for ( doc in it )
                    {
                        val temp = doc.toObject(Book::class.java)
                        tempArray.add(temp)
                    }
                    mGetCategoryBookSuccessListener.getCategoryBookSuccess(tempArray)
                }
                else
                {
                    mGetCategoryBookFailureListener.getCategoryBookFailure()
                }
            }
            .addOnFailureListener {
                mGetCategoryBookFailureListener.getCategoryBookFailure()
            }
    }

    fun getDoubleCategoryBook(cat1: String, cat2: String)
    {
        FirebaseFirestore.getInstance().collection("books").whereArrayContainsAny("categories", listOf(cat1, cat2)).get()
            .addOnSuccessListener {
                if ( !it.isEmpty )
                {
                    var tempArray = arrayListOf<Book>()
                    for ( doc in it )
                    {
                        val temp = doc.toObject(Book::class.java)
                        tempArray.add(temp)
                    }
                    mGetDoubleCategoryBookSuccessListener.getDoubleCategoryBookSuccess(tempArray, cat2)
                }
                else
                {
                    mGetDoubleCategoryBookFailureListener.getDoubleCategoryBookFailure()
                }
            }
            .addOnFailureListener {
                mGetDoubleCategoryBookFailureListener.getDoubleCategoryBookFailure()
            }
    }

    fun getAllBooks()
    {
        FirebaseFirestore.getInstance().collection("books").get()
            .addOnSuccessListener {
                if ( !it.isEmpty )
                {
                    var tempArray = arrayListOf<Book>()
                    for ( doc in it )
                    {
                        val temp = doc.toObject(Book::class.java)
                        tempArray.add(temp)
                    }
                    mGetAllBooksSuccessListener.getAllBooksSuccess(tempArray)
                }
                else
                {
                    mGetAllBooksFailureListener.getAllBooksFailure()
                }
            }
            .addOnFailureListener {
                mGetAllBooksFailureListener.getAllBooksFailure()
            }
    }

    fun getAllEssentials()
    {
        FirebaseFirestore.getInstance().collection("essentials").get()
            .addOnSuccessListener {
                if ( !it.isEmpty )
                {
                    var tempArray = arrayListOf<Essential>()
                    for ( doc in it )
                    {
                        val temp = doc.toObject(Essential::class.java)
                        tempArray.add(temp)
                    }
                    mGetAllEssentialsSuccessListener.getAllEssentialsSuccess(tempArray)
                }
                else
                {
                    mGetAllEssentialsFailureListener.getAllEssentialsFailure()
                }
            }
            .addOnFailureListener {
                mGetAllEssentialsFailureListener.getAllEssentialsFailure()
            }
    }

    //utility functions
    fun setGetBookSuccessListener(lol: getBookSuccessListener)
    {
        this.mGetBookSuccessListener = lol
    }

    fun setGetBookFailureListener(lol: getBookFailureListener)
    {
        this.mGetBookFailureListener = lol
    }

    fun setGetCategoryBookSuccessListener(lol: getCategoryBookSuccessListener)
    {
        this.mGetCategoryBookSuccessListener = lol
    }

    fun setGetCategoryBookFailureListener(lol: getCategoryBookFailureListener)
    {
        this.mGetCategoryBookFailureListener = lol
    }

    fun setGetDoubleCategoryBookSuccessListener(lol: getDoubleCategoryBookSuccessListener)
    {
        this.mGetDoubleCategoryBookSuccessListener = lol
    }

    fun setGetDoubleCategoryBookFailureListener(lol: getDoubleCategoryBookFailureListener)
    {
        this.mGetDoubleCategoryBookFailureListener = lol
    }

    fun setGetAllBooksSuccessListener(lol: getAllBooksSuccessListener)
    {
        this.mGetAllBooksSuccessListener = lol
    }

    fun setGetAllBooksFailureListener(lol: getAllBooksFailureListener)
    {
        this.mGetAllBooksFailureListener = lol
    }

    fun setGetEssentialSuccessListener(lol: getEssentialSuccessListener)
    {
        this.mGetEssentialSuccessListener = lol
    }

    fun setGetEssentialFailureListener(lol: getEssentialFailureListener)
    {
        this.mGetEssentialFailureListener = lol
    }

    fun setGetAllEssentialsSuccessListener(lol: getAllEssentialsSuccessListener)
    {
        this.mGetAllEssentialsSuccessListener = lol
    }

    fun setGetAllEssentialsFailureListener(lol: getAllEssentialsFailureListener)
    {
        this.mGetAllEssentialsFailureListener = lol
    }

    fun setChangeAmountBookSuccessListener(lol: changeAmountBookSuccessListener)
    {
        this.mChangeAmountBookSuccessListener = lol
    }

    fun setChangeAmountEssentialSuccessListener(lol: changeAmountEssentialSuccessListener)
    {
        this.mChangeAmountEssentialSuccessListener = lol
    }

    fun setChangeAmountFailureListener(lol: changeAmountFailureListener)
    {
        this.mChangeAmountFailureListener = lol
    }

    //interfaces
    interface getBookSuccessListener
    {
        fun getBookSuccess(book: Book)
    }

    interface getBookFailureListener
    {
        fun getBookFailure()
    }

    interface getCategoryBookSuccessListener
    {
        fun getCategoryBookSuccess(bookArray: ArrayList<Book>)
    }

    interface getCategoryBookFailureListener
    {
        fun getCategoryBookFailure()
    }

    interface getDoubleCategoryBookSuccessListener
    {
        fun getDoubleCategoryBookSuccess(bookArray: ArrayList<Book>, cat2: String)
    }

    interface getDoubleCategoryBookFailureListener
    {
        fun getDoubleCategoryBookFailure()
    }

    interface getAllBooksSuccessListener
    {
        fun getAllBooksSuccess(bookArray: ArrayList<Book>)
    }

    interface getAllBooksFailureListener
    {
        fun getAllBooksFailure()
    }

    interface getEssentialSuccessListener
    {
        fun getEssentialSuccess(essential: Essential)
    }

    interface getEssentialFailureListener
    {
        fun getEssentialFailure()
    }

    interface getAllEssentialsSuccessListener
    {
        fun getAllEssentialsSuccess(essentialArray: ArrayList<Essential>)
    }

    interface getAllEssentialsFailureListener
    {
        fun getAllEssentialsFailure()
    }

    interface changeAmountBookSuccessListener
    {
        fun changeAmountBookSuccess()
    }

    interface changeAmountEssentialSuccessListener
    {
        fun changeAmountEssentialSuccess()
    }

    interface changeAmountFailureListener
    {
        fun changeAmountFailure()
    }
}