package com.Kotlette.ecommerce.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.adapter.AdapterHome
import com.Kotlette.ecommerce.adapter.AdapterTransaction
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.file.FileManager
import com.Kotlette.ecommerce.item.ItemHome
import com.Kotlette.ecommerce.item.ItemTransaction
import com.Kotlette.ecommerce.model.ProductModel
import com.Kotlette.ecommerce.model.TransactionModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class HomeFragment : Fragment() {

    private lateinit var adapter : AdapterHome
    private lateinit var recyclerViewPopular : RecyclerView
    private lateinit var recyclerViewSale : RecyclerView
    private lateinit var recyclerViewAll : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //RecycleViewPopular
        val callbackPopular = object : HomeCallback {

            override fun onDataReceived(data: ArrayList<ItemHome>) {
                recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular)
                recyclerViewPopular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewPopular.setHasFixedSize(true)
                adapter = AdapterHome(data)
                recyclerViewPopular.adapter = adapter

            }
        }

        //RecycleViewSale
        val callbackSale = object : HomeCallback {
            override fun onDataReceived(data: ArrayList<ItemHome>) {
                recyclerViewSale = view.findViewById(R.id.recyclerViewSale)
                recyclerViewSale.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewSale.setHasFixedSize(true)
                var sale = arrayListOf<ItemHome>()

                for (i in 1..5 ) {
                    var r = Random.nextInt(0, data.size)
                    sale.add(data[r])
                    data.removeAt(r)
                }

                adapter = AdapterHome(sale)
                recyclerViewSale.adapter = adapter
            }
        }

        //RecycleViewAll
        val callbackAll = object : HomeCallback {
            override fun onDataReceived(data: ArrayList<ItemHome>) {
                recyclerViewAll = view.findViewById(R.id.recyclerViewAll)
                recyclerViewAll.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewAll.setHasFixedSize(true)
                adapter = AdapterHome(data)
                recyclerViewAll.adapter = adapter
            }

        }

        getProduct(callbackPopular, 1)
        getProduct(callbackSale, 2)
        getProduct(callbackAll, 3)
    }

    private fun getProduct(callback: HomeCallback, choice: Int) {

        var homeArrayList = arrayListOf<ItemHome>()
        var query = ""

        when (choice) {
            1 -> query = "select Pname, Price, ImageP from Product ORDER BY Quantity DESC;"
            2, 3 -> query = "select Pname, Price, ImageP from Product;"
            4 -> query = "select Pname, Price, ImageP from Product;"
        }


        ClientNetwork.retrofit.select(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.v("SELECT", "Response successful")
                        val resultSet = response.body()?.getAsJsonArray("queryset")
                        if (resultSet != null && resultSet.size() > 0) {
                            for (result in resultSet) {

                                val imageCall = object : ImageCallback {
                                    override fun onDataReceived(data: Bitmap?) {
                                        val p = Gson().fromJson(result, ProductModel::class.java)
                                        homeArrayList.add(ItemHome(null, p.name, data, p.price))
                                        adapter.notifyDataSetChanged()
                                    }
                                }
                                getImage(result.asJsonObject, imageCall)
                            }
                            callback.onDataReceived(homeArrayList)
                        } else {
                            Log.v("SELECT", "No tuples on Transaction table")
                            callback.onDataReceived(homeArrayList) // Nessun risultato trovato
                        }
                    } else {
                        callback.onDataReceived(homeArrayList) // Errore del server
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback.onDataReceived(homeArrayList) // Impossibile raggiungere il server
                    Log.v("SELECT", "Response failed")
                }
            }
        )
    }

    private fun getImage(jsonObject: JsonObject, callback: ImageCallback) {

        val url: String = jsonObject.get("ImageP").asString
        println(url)
        var image: Bitmap? = null

        ClientNetwork.retrofit.getAvatar(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful) {
                        Log.v("IMAGE", "Response successful")
                        if (response.body()!=null) {
                            println(response.body()?.byteStream())
                            image = BitmapFactory.decodeStream(response.body()?.byteStream())
                            callback.onDataReceived(image)
                        }
                    } else {
                        Log.v("IMAGE", "Response failed")
                        callback.onDataReceived(image)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback.onDataReceived(image)
                }

            }
        )
    }

    interface HomeCallback {
        fun onDataReceived(data: ArrayList<ItemHome>)

    }

    interface ImageCallback {
        fun onDataReceived(data: Bitmap?)

    }

}