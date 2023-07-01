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

        // Inizializza il selettore spinner
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

        // Lister della selezione dello spinner
        spinner.onItemSelectedListener = this



        // Callback per il recupero dei dati
        val callback = object : DataCallback {
            override fun onDataReceived(data: String?) {
                // Estrae i dati ricevuti (username e payMethod) dalla stringa
                val username = data?.split(" ")?.get(0)
                val payMethod = data?.split(" ")?.get(1)

                println(username)

                // Legge l'email dal file
                val data = context?.let { FileManager(it) }
                val email = data?.readFromFile("Email.txt")

                // Aggiorna le visualizzazioni con i dati ricevuti
                binding.textViewU.text = username
                binding.textViewE.text = email
                binding.textViewM.text = payMethod
            }

        }

        getData(callback)

        // Gestisce il click sul pulsante per cambiare password
        binding.buttonPass.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            val myFragment = ChangePassFragment()
            fragmentTransaction?.replace(R.id.fragmentContainerView, myFragment)
            fragmentTransaction?.addToBackStack("fragment ChangPass")
            fragmentTransaction?.commit()

        }

        // Gestisce il click sul pulsante per le transazioni
        binding.buttonTransaction.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            val myFragment = TransactionFragment()
            fragmentTransaction?.replace(R.id.fragmentContainerView, myFragment)
            fragmentTransaction?.addToBackStack("fragment Transaction")
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
        category.writeToFile("favorite.txt", Choice)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}