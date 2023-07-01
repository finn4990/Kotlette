package com.Kotlette.ecommerce.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.adapter.AdapterHome
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.databinding.FragmentSearchBinding
import com.Kotlette.ecommerce.file.FileManager
import com.Kotlette.ecommerce.item.ItemHome
import com.Kotlette.ecommerce.model.ProductModel
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random


class SearchFragment : Fragment(){

    /*private lateinit var adapterCategory : AdapterHome

    private var recyclerViewCategory : RecyclerView? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSearchBinding.inflate(layoutInflater)
        val view = binding.root

        binding.buttonSearch.setOnClickListener {
            val productName = binding.editTextSearch.text.toString()

            if(productName.isNotBlank()){
                searchProduct(productName)
            } else{
                Toast.makeText(requireContext(), "Fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        /*val spinner: Spinner = binding.spinner
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.Category,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

            spinner.onItemSelectedListener = this
        }*/

        return view
    }

    fun searchProduct(name : String){
        val query = "SELECT * FROM Product WHERE Pname = '${name}';"

        ClientNetwork.retrofit.select(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                            val result = response.body()?.getAsJsonArray("queryset")
                            val imageCall = object : ImageCallback {
                                override fun onDataReceived(data: Bitmap?) {
                                    val productCall = object : SearchCallback {
                                        override fun onDataReceived(product: ItemHome) {
                                            val fragmentManager = activity?.supportFragmentManager
                                            val fragmentTransaction = fragmentManager?.beginTransaction()
                                            fragmentTransaction?.replace(R.id.fragmentContainerView, DetailFragment(product))
                                            fragmentTransaction?.addToBackStack("Fragment Detail")
                                            fragmentTransaction?.commit()
                                        }
                                    }
                                    val p = Gson().fromJson(result!![0], ProductModel::class.java)
                                    val productSearched = ItemHome(p.code?.toInt(), p.name, data, p.price?.times((100- p.sale!!))?.div(100), p.description, p.quantity)
                                    productCall.onDataReceived(productSearched)
                                }
                            }
                            getImage(result!![0].asJsonObject, imageCall)
                        } else {
                            Toast.makeText(requireContext(), "Wrong product name", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.v("SELECT", "Server error")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.v("SELECT", "Can't reach the server")

                }
            }
        )
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callbackCategory = object : SearchCallback {
            override fun onDataReceived(data: ArrayList<ItemHome>) {
                recyclerViewCategory = view.findViewById(R.id.RecyclerViewCategory)
                recyclerViewCategory?.layoutManager = LinearLayoutManager(context)
                recyclerViewCategory?.setHasFixedSize(true)
                adapterCategory = AdapterHome(data)
                println("Adapter Info, $adapterCategory")
                recyclerViewCategory?.adapter = adapterCategory
                adapterCategory.setOnItemClickListener(object: AdapterHome.OnItemClickListener{
                    override fun onItemClick(position: Int) {

                        val fragmentManager = activity?.supportFragmentManager
                        val fragmentTransaction = fragmentManager?.beginTransaction()

                        fragmentTransaction?.replace(R.id.fragmentContainerView, DetailFragment(data[position]))
                        fragmentTransaction?.addToBackStack("Fragment Detail")
                        fragmentTransaction?.commit()
                        Toast.makeText(context,"Clicked an Item", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        getList(callbackCategory)
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {

        val category = FileManager(requireContext())
        val choice = parent.getItemAtPosition(pos).toString()
        category.writeToFile("category.txt", choice)

        val callbackCategory = object : SearchCallback {
            override fun onDataReceived(data: ArrayList<ItemHome>) {
                recyclerViewCategory = view.findViewById(R.id.RecyclerViewCategory)
                recyclerViewCategory?.layoutManager = LinearLayoutManager(context)
                recyclerViewCategory?.setHasFixedSize(true)
                adapterCategory = AdapterHome(data)
                println("Adapter Info, $adapterCategory")
                recyclerViewCategory?.adapter = adapterCategory
                adapterCategory.setOnItemClickListener(object: AdapterHome.OnItemClickListener{
                    override fun onItemClick(position: Int) {

                        val fragmentManager = activity?.supportFragmentManager
                        val fragmentTransaction = fragmentManager?.beginTransaction()

                        fragmentTransaction?.replace(R.id.fragmentContainerView, DetailFragment(data[position]))
                        fragmentTransaction?.addToBackStack("Fragment Detail")
                        fragmentTransaction?.commit()
                        Toast.makeText(context,"Clicked an Item", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        getList(callbackCategory)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    fun getList(callback: SearchCallback){
        val categoryArrayList = arrayListOf<ItemHome>()

        val fileCat = FileManager(requireContext())
        val category = fileCat.readFromFile("category.txt")

        val query = "SELECT PID, Pname, Price, ImageP, Description FROM Product WHERE Category = '${category}'"

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
                                        categoryArrayList.add(ItemHome(p.code?.toInt(), p.name, data, p.price, p.description))
                                        adapterCategory.notifyDataSetChanged()
                                    }
                                }
                                getImage(result.asJsonObject, imageCall)
                            }
                            callback.onDataReceived(categoryArrayList)
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
    }*/

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
                            //println(response.body()?.byteStream())
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

    interface SearchCallback {
        fun onDataReceived(data: ItemHome)
    }

    interface ImageCallback {
        fun onDataReceived(data: Bitmap?)
    }

}





