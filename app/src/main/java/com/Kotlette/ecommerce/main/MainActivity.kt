package com.Kotlette.ecommerce.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Carica il primo fragment
        replaceFragment(HomeFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    //Metodo di interazione con il menu di navigazione
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> replaceFragment(SearchFragment())
            R.id.account -> replaceFragment(ProfileFragment())
            R.id.home -> replaceFragment(HomeFragment())
            R.id.cart -> replaceFragment(CartFragment())
        }
        return super.onOptionsItemSelected(item)
    }

    //Metodo per aggiornare il contenuto del FragmentContainerView
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentContainerView,fragment)
        fragmentTransaction.addToBackStack("new fragment")
        fragmentTransaction.replace(R.id.fragmentContainerView,fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        fragmentManager.popBackStack()
    }
}