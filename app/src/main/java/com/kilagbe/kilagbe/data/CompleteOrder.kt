package com.kilagbe.kilagbe.data

data class CompleteOrder(val customeruid: String, val timestamp: String) {
    var cart: Cart? = null
    var orderId: String? = null
    var status: String? = null
    var address: String? = null
    var deliverymanuid: String? = null
    constructor():this("", "")
}