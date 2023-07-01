package com.Kotlette.ecommerce.item

object SingletonCart {
    private val cart = arrayListOf<ItemCart>()

    fun addToCart(item: ItemCart) {
        for ((i, element) in cart.withIndex()) {
            if (item.code == element.code) {
                addQuantity(i)
                return
            }
        }
        cart.add(item)
    }

    fun remFromCart(e: Int) {
        cart.removeAt(e)
    }

    fun addQuantity(position: Int) {
        if(cart[position].qty < 5 && cart[position].qty < cart[position].qtyProduct!!)
            cart[position].qty++
    }

    fun removeQuantity(position: Int) {
        if(cart[position].qty > 1)
            cart[position].qty--
    }

    fun clearCart() {
        cart.clear()
    }

    fun getCart(): ArrayList<ItemCart> {
        return cart
    }

    fun printCart() {
        for(e in cart)
            println("Oggetto: title - ${e.title}, price - ${e.price}")
    }

    fun getTotal() : Double {
        var sum : Double = 0.0
        for(e in cart)
            sum+= e.price!!.times(e.qty)
        return sum
    }

}