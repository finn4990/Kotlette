package com.Kotlette.ecommerce.clientweb

import com.Kotlette.ecommerce.item.ItemTransaction

interface DataCallback {

    fun onDataReceived(data: String?)
    fun onDataReceived(data: ArrayList<ItemTransaction>)

}