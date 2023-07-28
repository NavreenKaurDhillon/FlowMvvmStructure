package com.example.cqlsystechnologies.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cqlsystechnologies.model.ResponseModel
import com.example.cqlsystechnologies.repository.LoginRepository
import com.example.cqlsystechnologies.webServices.ApiState
import com.example.cqlsystechnologies.webServices.RetrofitService
import com.example.cqlsystechnologies.webServices.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository= LoginRepository(RetrofitService.create())

    val loginResponse = MutableStateFlow(ApiState(Status.NONE, ResponseModel(),""))


    fun login(map : HashMap<String, String>, securityKey: String){
        loginResponse.value = ApiState.loading()
        viewModelScope.launch {
            repository.login(map, securityKey)
                .catch {
                    loginResponse.value = ApiState.error(it.message.toString())
                }
                .collect{
                    loginResponse.value = ApiState.success(it.data)
                }

        }
    }


}