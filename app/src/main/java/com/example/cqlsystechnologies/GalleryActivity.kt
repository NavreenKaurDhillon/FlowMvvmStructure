package com.example.cqlsystechnologies

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cqlsystechnologies.adapter.GalleryAdapter
import com.example.cqlsystechnologies.databinding.ActivityGalleryBinding
import com.example.cqlsystechnologies.utils.ImagePickerUtil
import com.example.cqlsystechnologies.utils.SharedPreferenceManager
import com.example.cqlsystechnologies.viewModel.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedPreference : SharedPreferenceManager
    private var maps = HashMap<String, String>()
    private lateinit var imagePickerUtil: ImagePickerUtil
    private lateinit var coroutineScope : CoroutineScope

    private val imageUrls = ArrayList<String>()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        coroutineScope = CoroutineScope(Dispatchers.IO)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        imagePickerUtil = ImagePickerUtil(this, this)

        sharedPreference = SharedPreferenceManager(this)

        initListener()
        initObserver()

    }
    private fun initObserver() {

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initListener() {
        binding.textViewName.text = sharedPreference.getString("Name", "name")
        binding.floatingActionButton.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    private fun setRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3) // Change the span count as needed
        binding.recyclerView.adapter = GalleryAdapter(imageUrls)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    private fun showBottomSheetDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.upload_image_bottomsheet, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        // Set up click listeners for the buttons in the dialog
        view.findViewById<MaterialCardView>(R.id.iv_camera)?.setOnClickListener {
            // Do something when button 1 is clicked
            imagePickerUtil.pickImageFromCamera()
            dialog.dismiss()
        }
        view.findViewById<MaterialCardView>(R.id.iv_gallery)?.setOnClickListener {
            // Do something when button 2 is clicked
            imagePickerUtil.pickImageFromGallery()
            dialog.dismiss()
        }
        // Show the dialog
        dialog.show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            104 -> {
                if(data != null){
                    imageUrls.add(data.data.toString())
                    setRecyclerView()
                    Toast.makeText(this, data.data.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}