package com.Kotlette.ecommerce.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.databinding.FragmentLoginBinding
import com.Kotlette.ecommerce.file.FileManager
import com.Kotlette.ecommerce.main.MainActivity
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun openActivityMain(){
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if(email.isNotBlank() && password.isNotBlank()) {
                userLogin(email, password)
            } else{
                Toast.makeText(requireContext(), "Fill all the fields", Toast.LENGTH_SHORT).show()
            }


        }

        return view
    }

    private fun userLogin (email: String, password: String) {

        val data = context?.let { FileManager(it) }
        val query =
            "select * from User where Email = '${email}' and Password = '${password}';"

        ClientNetwork.retrofit.select(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                            data?.writeToFile("Email.txt", "${email}")
                            openActivityMain()
                            Toast.makeText(requireContext(), "Successful Login", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Wrong credentials", Toast.LENGTH_SHORT).show()
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

}