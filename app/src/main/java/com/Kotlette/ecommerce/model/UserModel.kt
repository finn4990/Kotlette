package com.Kotlette.ecommerce.model

import com.google.gson.annotations.SerializedName

data class UserModel(

    @SerializedName("Email")
    val email: String?,

    @SerializedName("Name")
    val name: String?,

    @SerializedName("Password")
    val password: String?,

    @SerializedName("PayMethod")
    val payMethod: String?,

    @SerializedName("Surname")
    val surname: String?,

    @SerializedName("Username")
    val username: String?,

)
