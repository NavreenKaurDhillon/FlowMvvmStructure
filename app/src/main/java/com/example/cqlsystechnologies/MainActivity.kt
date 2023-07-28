package com.example.cqlsystechnologies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.cqlsystechnologies.databinding.ActivityMainBinding
import com.example.cqlsystechnologies.utils.Keys
import com.example.cqlsystechnologies.utils.SharedPreferenceManager
import com.example.cqlsystechnologies.viewModel.LoginViewModel
import com.example.cqlsystechnologies.webServices.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedPreference : SharedPreferenceManager
    private var maps = HashMap<String, String>()

    lateinit var coroutineScope : CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        coroutineScope = CoroutineScope(Dispatchers.IO)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        sharedPreference = SharedPreferenceManager(this)

        initListener()
        initObserver()
    }

    private fun initListener() {

        binding.button.setOnClickListener {
            val pass = binding.editTextPass.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()

            val data = validateFormData(email, pass)

            if (data.isNullOrEmpty()){
                maps.clear()

                maps[Keys.EMAIL] = email
                maps[Keys.PASSWORD] = pass
                maps[Keys.DEVICE_TYPE] = ""
                maps[Keys.DEVICE_TOKEN] = ""

                coroutineScope.launch {
                    viewModel.login(maps, "BahamaEats")
                }

            }else{
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.loginResponse.collect {
                when (it.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        it.data?.let { it1 ->
                            Toast.makeText(this@MainActivity, it1.message, Toast.LENGTH_SHORT).show()
                            it1.body?.fullName?.let { it2 ->
                                sharedPreference.saveString("Name", it2)
                            }
                            sharedPreference.saveString("profile_img", it1.body!!.photo)


                            startActivity(Intent(this@MainActivity, GalleryActivity::class.java))
                        }

                    }

                    else -> {
                        if (Status.NONE != it.status) {
                            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun validateFormData(email: String, pass: String): String? {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (!isEmailValid) {
            return "Invalid email address."
        }

        val isPassValid = pass.isNotEmpty()
        if (!isPassValid) {
            return "Invalid password"
        }
        return null
    }
}