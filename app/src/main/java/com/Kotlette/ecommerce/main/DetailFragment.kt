package com.Kotlette.ecommerce.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.Kotlette.ecommerce.adapter.AdapterDetail
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.databinding.FragmentDetailBinding
import com.Kotlette.ecommerce.file.FileManager
import com.Kotlette.ecommerce.item.ItemCart
import com.Kotlette.ecommerce.item.ItemDetail
import com.Kotlette.ecommerce.item.ItemHome
import com.Kotlette.ecommerce.item.SingletonCart
import com.Kotlette.ecommerce.model.ReviewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFragment(private val product: ItemHome) : Fragment() {

    private lateinit var adapter : AdapterDetail
    private lateinit var recyclerView : RecyclerView

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
        val binding = FragmentDetailBinding.inflate(layoutInflater)
        val view = binding.root
        val data = context?.let { FileManager(it) }

        binding.imageProduct.setImageBitmap(product?.image)
        binding.titleProduct.text = product?.title
        binding.priceProduct.text = "Prezzo: " + product?.price.toString() + "€"
        binding.description.text = product?.description

        binding.buttonRate.setOnClickListener{
            data?.writeToFile("Id.txt", "${product?.id}")
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            val myFragment = ReviewFragment()
            fragmentTransaction?.replace(R.id.fragmentContainerView, myFragment)
            fragmentTransaction?.addToBackStack("fragment Review")
            fragmentTransaction?.commit()
        }

        binding.addCart.setOnClickListener{
            if(product.quantity!! > 0) {
                SingletonCart.addToCart(
                    ItemCart(
                        product.id,
                        product.title,
                        product.image,
                        product.price,
                        1,
                        product.quantity
                    )
                )
                Toast.makeText(context, "Item added to the cart", Toast.LENGTH_SHORT).show()
                SingletonCart.printCart()
            } else {
                Toast.makeText(context, "No product available", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //RecycleView
        val callback2 = object : DetailCallback {
            override fun onDataReceived(data: ArrayList<ItemDetail>) {
                recyclerView = view.findViewById(R.id.recyclerViewCart)
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.setHasFixedSize(true)
                adapter = AdapterDetail(data)
                recyclerView.adapter = adapter
            }
            override fun onImageReceived(image: Bitmap?) {}
        }

        getProduct(callback2, 2, product?.id)


    }

    private fun getProduct(callback: DetailCallback, choice: Int, id: Int?) {

        val detailArrayList = arrayListOf<ItemDetail>()
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

                                2 -> { for (result in resultSet) {
                                        val p = Gson().fromJson(result, ReviewModel::class.java)
                                        detailArrayList.add(ItemDetail(null, p.comment, p.rating))
                                    }
                                    callback.onDataReceived(detailArrayList)
                                }
                            }
                        } else {
                            callback.onDataReceived(detailArrayList) // Nessun risultato trovato
                        }
                    } else {
                        callback.onDataReceived(detailArrayList) // Errore del server
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback.onDataReceived(detailArrayList) // Impossibile raggiungere il server
                    Log.v("SELECT", "Response failed")
                }
            }
        )

    }

    private fun getImage(jsonObject: JsonObject?, callback: ImageCallback) {

        val url: String? = jsonObject?.get("ImageP")?.asString
        println(url)
        var image: Bitmap? = null

        if (url != null) {
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
    }

    interface ImageCallback {
        fun onDataReceived(data: Bitmap?)
    }

    interface DetailCallback {
        fun onDataReceived(data: ArrayList<ItemDetail>)
        fun onImageReceived(image: Bitmap?)
    }
}