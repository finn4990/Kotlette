package com.Kotlette.ecommerce

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.Kotlette.ecommerce.databinding.FragmentRegisterBinding
import android.widget.Toast

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

                openActivityLogin()
                Toast.makeText(requireContext(), "Ti sei registrato", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(requireContext(), "Devi inserire tutti i campi", Toast.LENGTH_SHORT).show()
            }
        }

        return view

    }
}