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
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random


class SearchFragment : Fragment(), AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSearchBinding.inflate(layoutInflater)
        val view = binding.root

        val spinner: Spinner = binding.spinner
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
        }

        return view
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {

        val category = FileManager(requireContext())
        val Choice = parent.getItemAtPosition(pos).toString()
        category.writeToFile("category.txt", Choice)

        val fragmentManager = getActivity()?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()

        val myFragment = CatSearchFragment()
        fragmentTransaction?.add(R.id.fragmentContainerView2, myFragment)
        fragmentTransaction?.addToBackStack("fragment Category Search")
        fragmentTransaction?.commit()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

}





