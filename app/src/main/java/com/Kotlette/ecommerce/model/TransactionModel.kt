package com.Kotlette.ecommerce.model

import com.google.gson.annotations.SerializedName

data class TransactionModel(

    @SerializedName("EmailT")
    val email: String?,

    @SerializedName("TID")
    val code: String?,

    @SerializedName("TDate")
    val date: String?,

    @SerializedName("Value")
    val total: String?,

)
