package com.Kotlette.ecommerce.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.clientweb.DataCallback
import com.Kotlette.ecommerce.databinding.FragmentProfileBinding
import com.Kotlette.ecommerce.file.FileManager
import com.Kotlette.ecommerce.model.UserModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment(), AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProfileBinding.inflate(layoutInflater)
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
        }

        spinner.onItemSelectedListener = this




        val callback = object : DataCallback {
            override fun onDataReceived(data: String?) {
                val username = data?.split(" ")?.get(0)
                val payMethod = data?.split(" ")?.get(1)

                println(username)

                val data = context?.let { FileManager(it) }
                val email = data?.readFromFile("Email.txt")

                binding.textViewU.text = username
                binding.textViewE.text = email
                binding.textViewM.text = payMethod
            }

        }

        getData(callback)

        binding.buttonPass.setOnClickListener {
            val fragmentManager = getActivity()?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            val myFragment = ChangePassFragment()
            fragmentTransaction?.replace(R.id.frame_layout, myFragment)
            fragmentTransaction?.addToBackStack("fragment ChangPass")
            fragmentTransaction?.commit()

        }

        return view
    }

    fun getData(callback: DataCallback) {
        val data = context?.let { FileManager(it) }
        val email = data?.readFromFile("Email.txt")

        val query = "SELECT Username, PayMethod FROM User WHERE Email = '${email}' "
        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val resultSet = response.body()?.getAsJsonArray("queryset")
                    if (resultSet != null && resultSet.size() == 1) {
                        val userModel = Gson().fromJson(resultSet[0], UserModel::class.java)
                        val username = userModel.username
                        val payMethod = userModel.payMethod
                        val res = "$username $payMethod"
                        callback.onDataReceived(res)
                    } else {
                        callback.onDataReceived(null) // Nessun risultato trovato
                    }
                } else {
                    callback.onDataReceived(null) // Errore del server
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onDataReceived(null) // Impossibile raggiungere il server
            }
        })
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val category = FileManager(requireContext())
        val Choice = parent.getItemAtPosition(pos).toString()
        category.writeToFile("category.txt", Choice)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}