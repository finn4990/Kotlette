package com.Kotlette.ecommerce

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.Kotlette.ecommerce.databinding.FragmentLoginBinding
import com.Kotlette.ecommerce.UserInstance


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

            if(email.isNotBlank() && password.isNotBlank()){
                /*val dbHelper = DatabaseHelper(requireContext())
                val db = dbHelper.writableDatabase

                val selection = "email = ? AND password = ?"
                val selectionArgs = arrayOf(email, password)

                val cursor = db.rawQuery("SELECT * FROM user WHERE $selection", selectionArgs)

                if (cursor != null && cursor.moveToFirst()) {
                    openActivityMain()
                    Toast.makeText(requireContext(), "Login avvenuto con successo", Toast.LENGTH_SHORT).show()
                } else {

                    Toast.makeText(requireContext(), "Credenziali errante, riprova", Toast.LENGTH_SHORT).show()
                }

                cursor?.close()
                db.close()*/
                openActivityMain()
                Toast.makeText(requireContext(), "Login avvenuto con successo", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(requireContext(), "Devi inserire tutti i campi", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

}