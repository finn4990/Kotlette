package com.Kotlette.ecommerce.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    // Metodo per gestire il click del pulsante di login
    private fun LoginButton(){
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            val myFragment1 = LoginFragment()
            fragmentTransaction.add(R.id.fragmentContainerView, myFragment1)
            fragmentTransaction.addToBackStack("fragment login")
            fragmentTransaction.commit()
            binding.Cl1?.visibility = View.INVISIBLE

        }

        // Metodo per gestire il click del pulsante di registrazione
        binding.buttonRegister.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            val myFragment = RegisterFragment()
            fragmentTransaction.add(R.id.fragmentContainerView, myFragment)
            fragmentTransaction.addToBackStack("fragment register")
            fragmentTransaction.commit()
            binding.Cl1?.visibility = View.INVISIBLE

        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        LoginButton()       // Chiamata al metodo per gestire i pulsanti di login e registrazione

    }
    // Override del metodo onBackPressed per gestire il comportamento del back button
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        fragmentManager.popBackStack()
        LoginButton()
    }
}