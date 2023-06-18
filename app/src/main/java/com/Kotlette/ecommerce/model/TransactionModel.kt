package com.Kotlette.ecommerce.model

import com.google.gson.annotations.SerializedName

data class TransactionModel(

    @SerializedName("TID")
    val code: String?,

    @SerializedName("TDate")
    val date: String?,

    @SerializedName("Value")
    val total: String?,

)
