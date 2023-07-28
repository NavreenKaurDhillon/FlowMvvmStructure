package com.example.cqlsystechnologies.repository

import com.example.cqlsystechnologies.model.ResponseModel
import com.example.cqlsystechnologies.webServices.ApiState
import com.example.cqlsystechnologies.webServices.RetrofitAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LoginRepository(private val apiService: RetrofitAPI) {

    suspend fun login(loginDetail : Map<String, String>, securityKey: String): Flow<ApiState<ResponseModel>> {
        return flow {
            val checklist = apiService.login(securityKey,loginDetail)
            emit(ApiState.success(checklist))
        }.flowOn(Dispatchers.IO)
    }
}