package com.Kotlette.ecommerce.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.databinding.FragmentConfirmBinding
import com.Kotlette.ecommerce.file.FileManager
import com.Kotlette.ecommerce.item.ItemCart
import com.Kotlette.ecommerce.item.SingletonCart
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ConfirmFragment : Fragment() {

    private lateinit var binding: FragmentConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmBinding.inflate(layoutInflater)
        val view = binding.root

        // Imposta il prezzo totale nel layout
        binding.totalPrice.text = "Total: ${SingletonCart.getTotal()}"

        // Gestisci il click sul pulsante di conferma transazione
        binding.confirmTransaction.setOnClickListener {

            // Aggiorna i prodotti nel database
            for (element in SingletonCart.getCart())
                updateProduct(element)
            // Inserisce la transazione nel database
            insertTransaction()
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            val myFragment = TransactionFragment()
            fragmentTransaction?.replace(R.id.fragmentContainerView, myFragment)
            fragmentTransaction?.addToBackStack("fragment Transaction")
            fragmentTransaction?.commit()

        }
        binding.denyTransaction.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        return view
    }

    private fun insertTransaction() {

        // Ottiene l'email dal file locale
        val data = context?.let { FileManager(it) }
        val email = data?.readFromFile("Email.txt")

        // Crea la query per inserire la transazione nel database
        val query = "INSERT INTO Transaction (EmailT, Value, TDate) VALUES ('${email}', '${SingletonCart.getTotal()}', '${SimpleDateFormat("yyyy-MM-dd").format(Date())}')"

        ClientNetwork.retrofit.insert(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful) {
                        Toast.makeText(requireContext(), "Order Completed!", Toast.LENGTH_SHORT).show()
                        SingletonCart.clearCart()
                    }else{
                        Log.v("INSERT", "Error!")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.v("INSERT", "Failed!")
                }
            }
        )
    }

    private fun updateProduct(item: ItemCart) {
        // Crea la query per aggiornare il prodotto nel database
        val query = "UPDATE Product SET Quantity = '${item.qtyProduct!!-item.qty}' WHERE PID = '${item.code}';"

        ClientNetwork.retrofit.update(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful) {
                        Toast.makeText(requireContext(), "Product updated", Toast.LENGTH_SHORT).show()
                    }else{
                        Log.v("UPDATE", "Error!")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.v("UPDATE", "Failed!")
                }
            }
        )
    }

}