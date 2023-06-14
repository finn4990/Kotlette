package com.Kotlette.ecommerce

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.Kotlette.ecommerce.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(layoutInflater)
        val view = binding.root

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


}