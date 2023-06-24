package com.Kotlette.ecommerce.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.adapter.AdapterDetail
import com.Kotlette.ecommerce.adapter.AdapterHome
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.databinding.FragmentDetailBinding
import com.Kotlette.ecommerce.file.FileManager
import com.Kotlette.ecommerce.item.ItemDetail
import com.Kotlette.ecommerce.item.ItemHome
import com.Kotlette.ecommerce.model.ProductModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class DetailFragment : Fragment() {

    private lateinit var adapter : AdapterDetail
    private lateinit var recyclerView : RecyclerView
    private lateinit var detailArrayList : ArrayList<ItemDetail>
    private lateinit var product : ItemHome

    lateinit var comment: Array<String>
    lateinit var iconUser: Array<Int>
    lateinit var vote: Array<Double>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        val data = context?.let { FileManager(it) }
        var id :Int

        setFragmentResultListener("Product") { requestKey, bundle ->
            var id = bundle.getInt("bundleId")
            var title = bundle.getString("bundleTitle")
            var price = bundle.getDouble("bundlePrice")
            var image = bundle.getString("bundleImage")
            product = ItemHome(id, title, null, price)
            binding.titleProduct.text = product.title


            val callback = object : DetailCallback {
                override fun onDataReceived(data: ArrayList<ItemDetail>) {}
                override fun onImageReceived(image: Bitmap?) {
                    binding.imageProduct.setImageBitmap(image)
                }
            }

            getProduct(callback, 1, product.id!!)
        }


        binding.buttonRate.setOnClickListener{
            data?.writeToFile("Id.txt", "${product.id}")
            val fragmentManager = getActivity()?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            val myFragment = ReviewFragment()
            fragmentTransaction?.replace(R.id.frame_layout, myFragment)
            fragmentTransaction?.addToBackStack("fragment Review")
            fragmentTransaction?.commit()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //RecycleViewPopular
        val callbackAll = object : DetailCallback {
            override fun onDataReceived(data: ArrayList<ItemDetail>) {
                recyclerView = view.findViewById(R.id.recyclerViewAll)
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.setHasFixedSize(true)
                adapter = AdapterDetail(data)
                recyclerView.adapter = adapter
            }
            override fun onImageReceived(image: Bitmap?) {}
        }


    }

    private fun getProduct(callback: DetailCallback, choice: Int, id: Int) {

        val homeArrayList = arrayListOf<ItemDetail>()
        val sale = arrayListOf<JsonObject>()
        var query = ""

        when (choice) {
            1 -> query = "select ImageP from Product where PID = '${id}';"
            2 -> query = "select Comment, Rating from Review where PIDR = '${id}';"
        }

        ClientNetwork.retrofit.select(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.v("SELECT", "Response successful")
                        val resultSet = response.body()?.getAsJsonArray("queryset")
                        if (resultSet != null && resultSet.size() > 0) {
                            when (choice) {
                                1 -> {
                                    val imageCall = object : ImageCallback {
                                        override fun onDataReceived(data: Bitmap?) {
                                            callback.onImageReceived(data)
                                        }
                                    }
                                    getImage(resultSet[0].asJsonObject, imageCall)
                                }

                                2 -> {

                                }
                            }
                        } else {
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
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Log.v("IMAGE", "Response successful")
                        if (response.body() != null) {
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

    interface ImageCallback {
        fun onDataReceived(data: Bitmap?)
    }

    interface DetailCallback {
        fun onDataReceived(data: ArrayList<ItemDetail>)
        fun onImageReceived(image: Bitmap?)
    }
}