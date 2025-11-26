package com.example.baseproject.activities

import android.Manifest
import android.app.ComponentCaller
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.baseproject.R
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityHomeBinding
import com.example.baseproject.utils.showToast
import kotlinx.coroutines.MainScope
import org.opencv.android.CameraActivity

class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    val requestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                //openCamera()
            } else {
                showToast("Permission Denied")
            }
        }

    private var mCamera: Camera? = null

    private lateinit var imageView: ImageView

    companion object {
        private const val PICK_IMAGE = 1
        private const val REQUEST_IMAGE_CAPTURE = 2
        private const val STORAGE_REQUEST_CODE = 101
        private const val TAG = "HomeActivity"
    }

    override fun initData() {

    }

    override fun initView() {
    }

    override fun initActionView() {
        binding.btnUpload.setOnClickListener { openGallery() }
        binding.btnCamera.setOnClickListener { dispatchTakePictureIntent() }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE)
    }

    private fun requestStorageAndCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED -> {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    STORAGE_REQUEST_CODE
                )
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {

            }

            else -> {
                requestLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val imageBitmap = data?.extras?.get("data") as Bitmap
                imageView.setImageBitmap(imageBitmap)
            }
        }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null  ){
            val selectedImageUri = data.data
            if (selectedImageUri != null){
                progressImage(selectedImageUri)
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ){
            val selectedImageUri = data?.data
            if (selectedImageUri != null){
                progressImage(selectedImageUri)
            }

        }

    }

    //mm
    private fun progressImage(image: Uri){
        try {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("image", image.toString())
            startActivity(intent)
        } catch (e: Exception){
            Log.d(TAG, "Loi: ${e.message}")
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            takePictureLauncher.launch(takePictureIntent)
        } catch (e: ActivityNotFoundException) {
            Log.d(TAG, "Loi ko bat duoc camera")
        }
    }



}