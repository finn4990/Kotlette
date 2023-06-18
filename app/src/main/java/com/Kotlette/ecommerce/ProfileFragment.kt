package com.Kotlette.ecommerce

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.databinding.FragmentProfileBinding
import com.Kotlette.ecommerce.file.FileManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {
    private var username: String? = null
    private var payMethod: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(layoutInflater)
        val view = binding.root

        val res = getData()

        val usePay = res?.split(" ")
        username = usePay?.get(0)
        payMethod = usePay?.get(1)

        val data = context?.let { FileManager(it) }
        val email = data?.readFromFile("Email.txt")

        binding.textViewU.text = username
        binding.textViewE.text = email
        binding.textViewM.text = payMethod

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

    fun getData(): String?{
        val data = context?.let { FileManager(it) }
        val email = data?.readFromFile("Email.txt")

        val query = "SELECT Username, PayMethod FROM User WHERE Email = '${email}' "
        val res : String?
        ClientNetwork.retrofit.select(query).enqueue(
            object : Callback<JsonObject> {
                 override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSet = response.body()?.getAsJsonArray("queryset")
                        if (resultSet != null && resultSet.size() == 1) {
                            username = resultSet[0].getAsJsonObject().get("Username").asString
                            payMethod = resultSet[0].getAsJsonObject().get("PayMethod").asString


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
        res = username + " " + payMethod
        return res
    }

}