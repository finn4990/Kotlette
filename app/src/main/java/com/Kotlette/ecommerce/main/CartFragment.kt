package com.Kotlette.ecommerce.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.adapter.AdapterCart
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.databinding.FragmentCartBinding
import com.Kotlette.ecommerce.file.FileManager
import com.Kotlette.ecommerce.item.ItemCart
import com.Kotlette.ecommerce.item.SingletonCart
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class CartFragment : Fragment() {

    private lateinit var adapter : AdapterCart
    private lateinit var recyclerView : RecyclerView
    private lateinit var binding : FragmentCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater)
        val view = binding.root

        // Gestisce il click sul pulsante di pagamento
        binding.buttonPayment.setOnClickListener{
            if(SingletonCart.getCart().size > 0) {
                val fragmentManager = activity?.supportFragmentManager
                fragmentManager?.popBackStack()
                val fragmentTransaction = fragmentManager?.beginTransaction()

                fragmentTransaction?.replace(R.id.fragmentContainerView, ConfirmFragment())
                fragmentTransaction?.addToBackStack("Fragment Detail")
                fragmentTransaction?.commit()
                Toast.makeText(context, "Payment process", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura la RecyclerView per visualizzare gli elementi del carrello
        recyclerView = view.findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        // Crea un adapter per la RecyclerView utilizzando la lista del carrello
        adapter = AdapterCart(SingletonCart.getCart())
        recyclerView.adapter = adapter
    }

}
