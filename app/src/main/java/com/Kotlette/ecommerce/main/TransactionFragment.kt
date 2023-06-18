package com.Kotlette.ecommerce.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.adapter.AdapterTransaction
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.file.FileManager
import com.Kotlette.ecommerce.item.ItemTransaction
import com.Kotlette.ecommerce.model.TransactionModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class TransactionFragment : Fragment() {

    private lateinit var adapter : AdapterTransaction
    private lateinit var recyclerView : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : TransactionCallback {

            override fun onDataReceived(data: ArrayList<ItemTransaction>) {
                val layoutManager = LinearLayoutManager(context)
                recyclerView = view.findViewById(R.id.recyclerView)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                adapter = AdapterTransaction(data)
                recyclerView.adapter = adapter
            }

        }

        var transactionArrayList = getPayments(callback)
    }

    private fun getPayments(callback: TransactionCallback) {

        val data = context?.let { FileManager(it) }
        val email = data?.readFromFile("Email.txt")

        var transactionArrayList = arrayListOf<ItemTransaction>()

        val query =
            "select TID, TDate, Value from Transaction where EmailT = '${email}';"

        ClientNetwork.retrofit.select(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.v("SELECT", "Response successful")
                        val resultSet = response.body()?.getAsJsonArray("queryset")
                        if (resultSet != null && resultSet.size() > 0) {
                            for (result in resultSet) {
                                val p = Gson().fromJson(result, TransactionModel::class.java)
                                transactionArrayList.add(ItemTransaction(p.code, p.date, p.total))
                            }
                            callback.onDataReceived(transactionArrayList)
                        } else {
                            Log.v("SELECT", "No tuples on Transaction table")
                            callback.onDataReceived(transactionArrayList) // Nessun risultato trovato
                        }
                    } else {
                        callback.onDataReceived(transactionArrayList) // Errore del server
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback.onDataReceived(transactionArrayList) // Impossibile raggiungere il server
                    Log.v("SELECT", "Response failed")
                }
            }
        )
    }

    interface TransactionCallback {
        fun onDataReceived(data: ArrayList<ItemTransaction>)

    }

}