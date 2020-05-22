package com.kilagbe.kilagbe.data


class Cart(var customeruid: String) {
    var status: String? = null
    var orderBookItems = arrayListOf<OrderItems>()
    var orderEssentialItems = arrayListOf<OrderItems>()
    var deliverymanuid: String? = null
    constructor():this("")
}