package com.kilagbe.kilagbe.data

data class Order(val customeruid: String, val itemid: String, val qty: Int, val status: String) {
    var deliverymanuid: String? = null
}