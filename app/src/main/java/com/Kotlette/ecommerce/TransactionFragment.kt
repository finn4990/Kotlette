package com.Kotlette.ecommerce

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.adapter.AdapterTransaction
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.item.ItemTransaction
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class TransactionFragment : Fragment() {

    private lateinit var adapter : AdapterTransaction
    private lateinit var recyclerView : RecyclerView
    private lateinit var transactionArrayList : ArrayList<ItemTransaction>

    lateinit var code: Array<String>
    lateinit var address: Array<String>
    lateinit var date: Array<Date>
    lateinit var total: Array<String>

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
        dataInitialize()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = AdapterTransaction(transactionArrayList)
        recyclerView.adapter = adapter
    }

    private fun getPayments (email: String) {

        val query =
            "select * from Transaction where EmailT = '${email}';"

        ClientNetwork.retrofit.select(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.v("SELECT", "Response successful")
                        if ((response.body()?.get("queryset") as JsonArray).size() > 0) {



                        } else {
                            Log.v("SELECT", "No tuples on Transaction table")
                        }
                    } else {
                        Log.v("SELECT", "Response was bad")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.v("SELECT", "Response failed")
                }
            }
        )
    }

    private fun dataInitialize() {

        transactionArrayList = arrayListOf<ItemTransaction>()

        code = arrayOf(
            "2UFTJ9C377EB",
            "51CZ4ULY121T",
            "L6474THG0SPV",
            "U5W9DQJM756X",
            "JJ890Y99ZXL4",
            "I2F2SCKF1C84",
            "1VMH00YL0ZXF",
            "8E5J3WFE26CI",
            "2UFTJ9C377EB",
            "51CZ4ULY121T",
            "L6474THG0SPV",
            "U5W9DQJM756X",
            "JJ890Y99ZXL4",
            "I2F2SCKF1C84",
            "1VMH00YL0ZXF",
            "8E5J3WFE26CI"
        )

        address = arrayOf(
            "Via Badia 9",
            "Via Tiepolo 15",
            "Piazza Navona 45A",
            "Srada Longevo 8",
            "Viale Regione Siciliana 77",
            "Via Cordova 6",
            "Via M. De Cervantes 2",
            "Piazza A. Arrigo 4",
            "Via Badia 9",
            "Via Tiepolo 15",
            "Piazza Navona 45A",
            "Srada Longevo 8",
            "Viale Regione Siciliana 77",
            "Via Cordova 6",
            "Via M. De Cervantes 2",
            "Piazza A. Arrigo 4"
        )

        date = arrayOf(
            Date("2552-05-05"),
            Date("2552-05-05"),
            Date("2552-05-05"),
            Date("2552-05-05"),
            Date("2552-05-05"),
            Date("2552-05-05"),
            Date("2552-05-05"),
            Date("2552-05-05"),
            Date("2552-05-05"),
            Date("2552-05-05"),
            Date("2552-05-05"),
            Date("2552-05-05"),
            Date("2552-05-05"),
            Date("2552-05-05"),
            Date("2552-05-05"),
            Date("2552-05-05")
        )

        total = arrayOf(
            "15,58€",
            "26,18€",
            "100,00€",
            "48,58€",
            "35,58€",
            "15,53€",
            "7,22€",
            "18,40€",
            "15,58€",
            "26,18€",
            "100,00€",
            "48,58€",
            "35,58€",
            "15,53€",
            "7,22€",
            "18,40€"
        )

        for(i in code.indices){

            val transaction = ItemTransaction(code[i], address[i], date[i], total[i])
            transactionArrayList.add(transaction)
        }

    }

}