package com.Kotlette.ecommerce.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.databinding.FragmentReviewBinding
import com.Kotlette.ecommerce.file.FileManager


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
            val idP = data?.readFromFile("Id.txt").toInt()
            if(review.isNotBlank()){
                InsertReview(rating, review, email, idP)
            }else{

            }
        }
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    private fun InsertReview(rating: Float, review: String, email: String?, idP: Int) {


        val query = "INSERT INTO Review () VALUES ('${email}', '${password}', '${name}', '${surname}', '${payment}', '${username}')"
    }


}