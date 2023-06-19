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
        //dataInitialize()

        //RecycleViewPopular
        /*recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular)
        recyclerViewPopular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPopular.setHasFixedSize(true)
        adapter = AdapterHome(data)
        recyclerViewPopular.adapter = adapter

        //RecycleViewSale
        recyclerViewSale = view.findViewById(R.id.recyclerViewSale)
        recyclerViewSale.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewSale.setHasFixedSize(true)
        adapter = AdapterHome(data)
        recyclerViewSale.adapter = adapter*/

        //RecycleViewAll
        val callback = object : HomeCallback {

            override fun onDataReceived(data: ArrayList<ItemHome>) {
                recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular)
                recyclerViewPopular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewPopular.setHasFixedSize(true)
                adapter = AdapterHome(data)
                recyclerViewPopular.adapter = adapter

                recyclerViewSale = view.findViewById(R.id.recyclerViewSale)
                recyclerViewSale.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewSale.setHasFixedSize(true)
                //adapter = AdapterHome(data)
                recyclerViewSale.adapter = adapter

                recyclerViewAll = view.findViewById(R.id.recyclerViewAll)
                recyclerViewAll.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewAll.setHasFixedSize(true)
                //adapter = AdapterHome(data)
                recyclerViewAll.adapter = adapter
            }

        }

        getProduct(callback)
    }

    private fun getProduct(callback: HomeCallback) {

        var homeArrayList = arrayListOf<ItemHome>()

        val query =
            "select Pname, Price, ImageP from Product;"

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
                                        homeArrayList.add(ItemHome(p.name, data, p.price))
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

    /*private fun getImage(url: String?): Int? {

        var image: Int? = null
        println(url)
        if (url != null) {
            ClientNetwork.retrofit.getAvatar(url).enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Log.v("IMAGE", "Response successful")
                            val result = response.body()
                            if (result != null) {
                                println(result)
                            } else {
                                Log.v("IMAGE", "No tuples on Transaction table") // Nessun risultato trovato
                            }
                        } else {
                            Log.v("IMAGE", "No tuples on Transaction table") // Errore del server
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.v("IMAGE", "Response failed") // Impossibile raggiungere il server
                    }
                })
            return image
        }else {
            Log.v("IMAGE", "No url")
            return null
        }
    }*/

    interface HomeCallback {
        fun onDataReceived(data: ArrayList<ItemHome>)

    }

    interface ImageCallback {
        fun onDataReceived(data: Bitmap?)

    }

    /*private fun dataInitialize() {

        homeArrayList = arrayListOf<ItemHome>()

        title = arrayOf(
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

        image = arrayOf(
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24
        )

        price = arrayOf(
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

        for(i in title.indices){

            val home = ItemHome(title[i], image[i], price[i])
            homeArrayList.add(home)
        }

    }*/

}