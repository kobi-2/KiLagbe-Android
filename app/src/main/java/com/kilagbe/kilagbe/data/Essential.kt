package com.kilagbe.kilagbe.data

data class Essential(val name: String, var price: Double) {
    var manufacturer: String? = null
    var amountInStock : Int? = null
    var photoUrl: String? = null
    var itemId: String? = null
    constructor():this("", 0.0)
}