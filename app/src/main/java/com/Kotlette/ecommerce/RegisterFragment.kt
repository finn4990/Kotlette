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
               /* val dbHelper = DatabaseHelper(requireContext())
                val db = dbHelper.writableDatabase

                val values = ContentValues()
                values.put("username", username)
                values.put("password", password)
                values.put("name", name)
                values.put("surname", surname)
                values.put("email", email)
                values.put("payment", payment)

                val rowId = db.insert("user", null, values)

                db.close()*/

                //openActivityLogin()
                registerUtente(email, password, name, surname, payment, username)
                Toast.makeText(requireContext(), "Ti sei registrato", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(requireContext(), "Devi inserire tutti i campi", Toast.LENGTH_SHORT).show()
            }
        }

        return view

    }

    private fun registerUtente (email: String, password: String,
                             name:String, surname: String,
                             payment: String, username: String) {

        Log.v("INSERT", "Step 2!")
        val query = "INSERT INTO utente VALUES ('dudu', 'dd', 'cc', 'ff', 'aa', 'bb')"

        ClientNetwork.retrofit.insert(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.v("INSERT", "Step 3!")
                    } else {
                        Log.v("INSERT", "Merda 2!")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.v("INSERT", "Una Merda!")

                }
            }
        )
    }
}