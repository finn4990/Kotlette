package com.Kotlette.ecommerce

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.databinding.FragmentChangePassBinding
import com.Kotlette.ecommerce.file.FileManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChangePassFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChangePassBinding.inflate(layoutInflater)
        val view = binding.root
        val data = context?.let { FileManager(it) }


        binding.buttonChange.setOnClickListener {
            val old_password = binding.editTextCurrentPsw.text.toString()
            val new_password = binding.editTextNewPsw.text.toString()
            val confirm_password = binding.editText2psw.text.toString()
            val email = data?.readFromFile("Email.txt")

            if(old_password.isNotBlank() && new_password.isNotBlank() && confirm_password.isNotBlank()){
                if(new_password == confirm_password){
                    if (email != null) {
                        updatePassword(email, old_password, new_password)
                    }
                }else{
                    Toast.makeText(requireContext(), "Confirm password error", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(requireContext(), "Fill all the fields", Toast.LENGTH_SHORT).show()
            }

        }
        return view
    }

    fun updatePassword(email: String, old_password: String, new_password: String){
        val queryC = "SELECT * FROM User WHERE  Email = '${email}' AND Password = '${old_password}';"
        val query = "UPDATE User SET Password = '${new_password}' WHERE email = '${email}'; "

        ClientNetwork.retrofit.insert(queryC).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if((response.body()?.get("queryset") as JsonArray).size() == 1){
                            ClientNetwork.retrofit.select(query).enqueue(
                                object : Callback<JsonObject> {
                                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                        if(response.isSuccessful) {
                                            Toast.makeText(requireContext(), "Password changed correctly", Toast.LENGTH_SHORT).show()
                                        }else{
                                            Log.v("UPDATE", "Error!")
                                        }
                                    }
                                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                        Log.v("UPDATE", "Failed!")
                                    }
                                }
                            )
                        }else{
                            Toast.makeText(requireContext(), "Current password isn't correct", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.v("SELECT", "Error!")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.v("SELECT", "Failed!")

                }
            }
        )
    }


}