package com.Kotlette.ecommerce.item

object SingletonCart {
    private val cart = arrayListOf<ItemCart>()

    fun addToCart(item: ItemCart) {
        for ((i, element) in cart.withIndex()) {
            if (item.code == element.code) {
                cart[i].qty++
                return
            }
        }
        cart.add(item)
    }

    fun remFromCart(e: Int) {
        cart.removeAt(e)
    }

    fun addQuantity(position: Int) {
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
        for(i in cart)
            println("Oggetto: title - ${i.title}, price - ${i.price}")
    }

}