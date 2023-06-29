package com.Kotlette.ecommerce.clientweb

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClientNetwork {

    val retrofit: UserAPI by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/webmobile/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserAPI::class.java)
    }

}