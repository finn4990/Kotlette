package com.Kotlette.ecommerce.model

import com.google.gson.annotations.SerializedName

data class ReviewModel(

    @SerializedName("Comment")
    val comment: String?,

    @SerializedName("Rating")
    val rating: String?,

    @SerializedName("UsernameR")
    val username: String?,

    @SerializedName("PIDR")
    val productID: String?
)
