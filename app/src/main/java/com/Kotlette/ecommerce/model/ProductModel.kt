package com.Kotlette.ecommerce.model

import com.google.gson.annotations.SerializedName

data class ProductModel(

    @SerializedName("PID")
    val code: String?,

    @SerializedName("Variant")
    val variant: String?,

    @SerializedName("Pname")
    val name: String?,

    @SerializedName("Category")
    val category: String?,

    @SerializedName("Description")
    val description: String?,

    @SerializedName("Value")
    val quantity: String?,

    @SerializedName("Price")
    val price: Double?,

    @SerializedName("ImageP")
    val image: String?,

    @SerializedName("Sale")
    val sale: Int?
)