package com.Kotlette.ecommerce

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.Kotlette.ecommerce.databinding.FragmentRegisterBinding
import android.widget.Toast
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun openActivityLogin(){
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonRegister.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()
            val name = binding.editTextName.text.toString()
            val surname = binding.editTextSurname.text.toString()
            val email = binding.editTextEmail.text.toString()
            val payment = binding.editTextPayment.text.toString()

            if(username.isNotBlank() && password.isNotBlank() && name.isNotBlank() && surname.isNotBlank() && email.isNotBlank() && payment.isNotBlank()){
                userRegister(email, password, name, surname, payment, username)
            } else{
                Toast.makeText(requireContext(), "Fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun userRegister (email: String, password: String,
                             name:String, surname: String,
                             payment: String, username: String) {

        val queryC = "SELECT * FROM User WHERE Email = '${email}';"
        val query = "INSERT INTO User VALUES ('${email}', '${password}', '${name}', '${surname}', '${payment}', '${username}')"

        ClientNetwork.retrofit.insert(queryC).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if((response.body()?.get("queryset") as JsonArray).size() == 1){
                            Toast.makeText(requireContext(), "Account already exists", Toast.LENGTH_SHORT).show()
                        }else{
                            ClientNetwork.retrofit.select(query).enqueue(
                               object : Callback<JsonObject> {
                                   override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                       if(response.isSuccessful) {
                                           Toast.makeText(requireContext(), "Account already exists", Toast.LENGTH_SHORT).show()
                                       }else{
                                           Log.v("INSERT", "Error!")
                                       }
                                   }
                                   override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                       Log.v("INSERT", "Failed!")
                                   }
                               }
                            )
                        }
                    } else {
                        Log.v("INSERT", "Error!")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.v("INSERT", "Failed!")

                }
            }
        )
    }
}