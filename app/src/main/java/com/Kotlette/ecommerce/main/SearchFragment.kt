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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSearchBinding.inflate(layoutInflater)
        val view = binding.root

        // Gestisce il click sul pulsante di ricerca
        binding.buttonSearch.setOnClickListener {
            val productName = binding.editTextSearch.text.toString()

            if(productName.isNotBlank()){
                searchProduct(productName)
            } else{
                Toast.makeText(requireContext(), "Fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    fun searchProduct(name : String){
        // Query per selezionare il prodotto con il nome specificato
        val query = "SELECT * FROM Product WHERE Pname = '${name}';"

        // Esegue la query sul server tramite Retrofit
        ClientNetwork.retrofit.select(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                            val result = response.body()?.getAsJsonArray("queryset")
                            // Callback per il recupero dell'immagine del prodotto
                            val imageCall = object : ImageCallback {
                                override fun onDataReceived(data: Bitmap?) {
                                    // Callback per il recupero del prodotto completo
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





