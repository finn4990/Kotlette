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
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.databinding.FragmentCatSearchBinding
import com.Kotlette.ecommerce.file.FileManager
import com.Kotlette.ecommerce.item.ItemHome
import com.Kotlette.ecommerce.model.ProductModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CatSearchFragment : Fragment() {

    private lateinit var adapterCategory : AdapterHome

    private lateinit var recyclerViewCategory : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cat_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCatSearchBinding.inflate(layoutInflater)
        val view = binding.root

        val callbackCategory = object : CatSearchFragment.CatSearchCallback {

            override fun onDataReceived(data: ArrayList<ItemHome>) {
                recyclerViewCategory = binding.RecyclerViewCategory
                recyclerViewCategory .layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewCategory .setHasFixedSize(true)
                adapterCategory = AdapterHome(data)
                recyclerViewCategory .adapter = adapterCategory

            }
        }

        getList(callbackCategory)
    }

    fun getList(callback: CatSearchFragment.CatSearchCallback){
        var categoryArrayList = arrayListOf<ItemHome>()

        val fileCat = FileManager(requireContext())
        val category = fileCat.readFromFile("category.txt")

        val query = "SELECT PID, Pname, Price, ImageP FROM Product WHERE Category = '${category}'"

        ClientNetwork.retrofit.select(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.v("SELECT", "Response successful")
                        val resultSet = response.body()?.getAsJsonArray("queryset")
                        if (resultSet != null && resultSet.size() > 0) {
                            for (result in resultSet) {
                                val imageCall = object : CatSearchFragment.ImageCallback {
                                    override fun onDataReceived(data: Bitmap?) {
                                        val p = Gson().fromJson(result, ProductModel::class.java)
                                        categoryArrayList.add(ItemHome(p.code?.toInt(), p.name, data, p.price, p.description))
                                        adapterCategory.notifyDataSetChanged()
                                    }
                                }
                                getImage(result.asJsonObject, imageCall)
                            }
                        } else {
                            Log.v("SELECT", "No tuples on Transaction table")
                            callback.onDataReceived(categoryArrayList)
                        }
                    } else {
                        callback.onDataReceived(categoryArrayList)
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback.onDataReceived(categoryArrayList)
                    Log.v("SELECT", "Response failed")
                }
            }
        )
    }

    private fun getImage(jsonObject: JsonObject, callback: CatSearchFragment.ImageCallback) {

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
                    callback.onDataReceived(image) // Impossibile raggiungere il server
                }

            }
        )
    }

    interface CatSearchCallback {
        fun onDataReceived(data: ArrayList<ItemHome>)
    }

    interface ImageCallback {
        fun onDataReceived(data: Bitmap?)
    }
}