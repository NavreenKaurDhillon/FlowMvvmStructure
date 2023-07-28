package com.example.cqlsystechnologies.utils

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class ImagePickerUtil(private val context: Context, private val activity: Activity) {

    val CAMERA_PERMISSION_REQUEST_CODE = 101
    val GALLERY_PERMISSION_REQUEST_CODE = 102
    val CAMERA_REQUEST_CODE = 103
    val GALLERY_REQUEST_CODE = 104

    lateinit var imageUri: Uri

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun pickImageFromGallery() {
        if (checkPermissionForGallery()) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activity.startActivityForResult(intent, GALLERY_REQUEST_CODE)
        } else {
            requestPermissionForGallery()
        }
    }

    fun pickImageFromCamera() {
        if (checkPermissionForCamera()) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
            imageUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            activity.startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            requestPermissionForCamera()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromCamera()
                } else {
                    Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
                }
            }
            GALLERY_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(context, "Gallery permission is required", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissionForGallery(): Boolean {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissionForCamera(): Boolean {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        return result == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermissionForGallery() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
            GALLERY_PERMISSION_REQUEST_CODE
        )
    }

    private fun requestPermissionForCamera() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }
}