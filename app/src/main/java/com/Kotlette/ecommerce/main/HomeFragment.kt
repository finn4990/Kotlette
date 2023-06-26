package com.Kotlette.ecommerce.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.adapter.AdapterHome
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.databinding.FragmentHomeBinding
import com.Kotlette.ecommerce.item.ItemHome
import com.Kotlette.ecommerce.model.ProductModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class HomeFragment : Fragment() {

    private lateinit var adapterPopular : AdapterHome
    private lateinit var adapterSale : AdapterHome
    private lateinit var adapterAll : AdapterHome

    private lateinit var recyclerViewPopular : RecyclerView
    private lateinit var recyclerViewSale : RecyclerView
    private lateinit var recyclerViewAll : RecyclerView

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        onCreate(savedInstanceState)
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //RecycleViewPopular
        val callbackPopular = object : HomeCallback {

            override fun onDataReceived(data: ArrayList<ItemHome>) {
                recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular)
                recyclerViewPopular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewPopular.setHasFixedSize(true)
                adapterPopular = AdapterHome(data)
                recyclerViewPopular.adapter = adapterPopular
                adapterPopular.setOnItemClickListener(object: AdapterHome.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        Toast.makeText(context,"Clicked an Item",Toast.LENGTH_SHORT).show()
                    }
                })

            }
        }

        //RecycleViewSale
        val callbackSale = object : HomeCallback {
            override fun onDataReceived(data: ArrayList<ItemHome>) {
                recyclerViewSale = view.findViewById(R.id.recyclerViewSale)
                recyclerViewSale.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewSale.setHasFixedSize(true)
                adapterSale = AdapterHome(data)
                recyclerViewSale.adapter = adapterSale
                adapterSale.setOnItemClickListener(object: AdapterHome.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        Toast.makeText(context,"Clicked an Item",Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        //RecycleViewAll
        val callbackAll = object : HomeCallback {
            override fun onDataReceived(data: ArrayList<ItemHome>) {
                recyclerViewAll = view.findViewById(R.id.recyclerViewAll)
                recyclerViewAll.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewAll.setHasFixedSize(true)
                adapterAll = AdapterHome(data)
                recyclerViewAll.adapter = adapterAll
                adapterAll.setOnItemClickListener(object: AdapterHome.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        setFragmentResult("Product", bundleOf(
                            "bundleId" to data[position].id,
                            "bundlePrice" to data[position].price,
                            "bundleTitle" to data[position].title
                            )
                        )
                        val fragmentManager = activity?.supportFragmentManager
                        val fragmentTransaction = fragmentManager?.beginTransaction()

                        fragmentTransaction?.replace(R.id.fragmentContainerView, DetailFragment())
                        fragmentTransaction?.addToBackStack("Fragment Detail")
                        fragmentTransaction?.commit()
                        Toast.makeText(context,"Clicked an Item",Toast.LENGTH_SHORT).show()
                    }
                })
            }

        }

        getProduct(callbackPopular, 1)
        getProduct(callbackSale, 2)
        getProduct(callbackAll, 3)

    }

    private fun getProduct(callback: HomeCallback, choice: Int) {

        val homeArrayList = arrayListOf<ItemHome>()
        val sale = arrayListOf<JsonObject>()
        var query = ""

        when (choice) {
            1 -> query = "select PID, Pname, Price, ImageP from Product ORDER BY Quantity DESC;"
            2, 3 -> query = "select PID, Pname, Price, ImageP from Product;"
            4 -> query = "select PID, Pname, Price, ImageP from Product;"
        }


        ClientNetwork.retrofit.select(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.v("SELECT", "Response successful")
                        val resultSet = response.body()?.getAsJsonArray("queryset")
                        if (resultSet != null && resultSet.size() > 0) {
                            when (choice) {
                                1 -> for (i in 0..4) {
                                        val imageCall = object : ImageCallback {
                                            override fun onDataReceived(data: Bitmap?) {
                                                val p = Gson().fromJson(resultSet[i], ProductModel::class.java)
                                                homeArrayList.add(ItemHome(p.code?.toInt(), p.name, data, p.price))
                                                adapterPopular.notifyDataSetChanged()
                                            }
                                        }
                                        getImage(resultSet[i].asJsonObject, imageCall)
                                    }

                                2 -> {
                                        for (i in 0..4) {
                                            var r = Random.nextInt(resultSet.size())
                                            sale.add(resultSet[r].asJsonObject)
                                            resultSet.remove(r)
                                        }
                                        for (i in sale) {
                                            val imageCall = object : ImageCallback {
                                                    override fun onDataReceived(data: Bitmap?) {
                                                        val p = Gson().fromJson(i, ProductModel::class.java)
                                                        println(p.price)
                                                        homeArrayList.add(ItemHome(p.code?.toInt(), p.name, data, p.price?.times((100-20))?.div(100)))
                                                        adapterSale.notifyDataSetChanged()
                                                    }
                                                }
                                            getImage(i, imageCall)
                                        }
                                }

                                3 -> for (result in resultSet) {
                                        val imageCall = object : ImageCallback {
                                            override fun onDataReceived(data: Bitmap?) {
                                                val p = Gson().fromJson(result, ProductModel::class.java)
                                                homeArrayList.add(ItemHome(p.code?.toInt(), p.name, data, p.price))
                                                adapterAll.notifyDataSetChanged()
                                            }
                                        }
                                        getImage(result.asJsonObject, imageCall)
                                    }
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
                            image = BitmapFactory.decodeStream(response.body()?.byteStream())
                            println(image)
                            callback.onDataReceived(image)
                        }
                    } else {
                        Log.v("IMAGE", "Response failed")
                        callback.onDataReceived(image)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback.onDataReceived(image) // Impossibile raggiungere il server
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