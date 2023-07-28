package com.example.cqlsystechnologies.webServices

import com.example.cqlsystechnologies.model.ResponseModel
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface RetrofitAPI {

    @PUT("login")
    @FormUrlEncoded
    suspend fun login(
        @Header("security_key") securityKey: String,
        @FieldMap loginDetail : Map<String, String>
    ):ResponseModel
}