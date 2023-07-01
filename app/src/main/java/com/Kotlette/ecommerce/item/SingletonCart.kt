package com.Kotlette.ecommerce.item

object SingletonCart {
    private val cart = arrayListOf<ItemCart>()

    // Aggiunge un elemento al carrello, se è già presente, incrementa la quantità
    fun addToCart(item: ItemCart) {
        for ((i, element) in cart.withIndex()) {
            if (item.code == element.code) {
                addQuantity(i)
                return
            }
        }
        cart.add(item)
    }

    // Rimuove un elemento dal carrello in base alla posizione
    fun remFromCart(e: Int) {
        cart.removeAt(e)
    }

    // Incrementa la quantità di un elemento nel carrello (se è inferiore a 5 e alla quantità massima disponibile del prodotto)
    fun addQuantity(position: Int) {
        if(cart[position].qty < 5 && cart[position].qty < cart[position].qtyProduct!!)
            cart[position].qty++
    }

    // Decrementa la quantità di un elemento nel carrello (se è superiore a 1)
    fun removeQuantity(position: Int) {
        if(cart[position].qty > 1)
            cart[position].qty--
    }

    // Svuota completamente il carrello
    fun clearCart() {
        cart.clear()
    }

    // Restituisce l'intero carrello come un ArrayList di oggetti ItemCart
    fun getCart(): ArrayList<ItemCart> {
        return cart
    }

    // Stampa i dettagli di ogni elemento presente nel carrello
    fun printCart() {
        for(e in cart)
            println("Oggetto: title - ${e.title}, price - ${e.price}")
    }

    // Calcola il totale dei prezzi degli elementi nel carrello
    fun getTotal() : Double {
        var sum : Double = 0.0
        for(e in cart)
            sum+= e.price!!.times(e.qty)
        return sum
    }

}