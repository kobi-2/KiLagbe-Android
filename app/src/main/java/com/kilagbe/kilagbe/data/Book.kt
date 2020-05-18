package com.kilagbe.kilagbe.data


data class Book(val name: String, var price: Double) {
    var authors = arrayListOf<String>()
    var categories =  arrayListOf<Categories>()
    lateinit var publisher: String
    var amountInStock : Int? = null
    constructor():this("", 0.0)
}