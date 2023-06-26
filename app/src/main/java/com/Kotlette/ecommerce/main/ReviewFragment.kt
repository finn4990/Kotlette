package com.Kotlette.ecommerce.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.clientweb.ClientNetwork
import com.Kotlette.ecommerce.databinding.FragmentReviewBinding
import com.Kotlette.ecommerce.file.FileManager
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReviewFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentReviewBinding.inflate(inflater, container,false )
        val view = binding.root
        val data = context?.let { FileManager(it) }

        binding.submitButton.setOnClickListener{
            val rating = binding.ratingBar.rating
            val review = binding.reviewEditText.text.toString()
            val email = data?.readFromFile("Email.txt")
            val idP = data?.readFromFile("Id.txt")?.toInt()
            if(review.isNotBlank()){
                if (idP != null) {
                    InsertReview(rating, review, email, idP)
                }
            }else{
                Toast.makeText(requireContext(), "Fill all the fields!", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun InsertReview(rating: Float, review: String, email: String?, idP: Int) {

        val query = "INSERT INTO Review VALUES ('${review}', '${rating}', '${email}', '${idP}')"

        ClientNetwork.retrofit.insert(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful) {
                        Toast.makeText(requireContext(), "Review submit!", Toast.LENGTH_SHORT).show()
                        activity?.supportFragmentManager?.popBackStack()
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


}