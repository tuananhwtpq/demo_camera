package com.example.baseproject.activities

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.example.baseproject.R
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityMainBinding
import org.opencv.android.OpenCVLoader
import kotlin.math.log

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private var imageUriString: String? = null

    override fun initData() {
        makeStatusBarTransparent()
        testOpenCV()
    }

    override fun initView() {
        imageUriString = intent.getStringExtra("image")
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            loadUserImage(imageUri)
            loadImage(imageUri)
        }
    }

    override fun initActionView() {
        //binding.test.setOnClickListener { testOpenCV() }
    }

    fun testOpenCV() {
        if (OpenCVLoader.initDebug()) {
            Toast.makeText(applicationContext, "Open CV load success", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, "Open CV load fail", Toast.LENGTH_LONG).show()
        }
    }

    fun makeStatusBarTransparent() {
        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            statusBarColor = getColor(R.color.colorError)
        }
    }

    private fun loadImage(imageUri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(imageUri)
            val userBitMap = BitmapFactory.decodeStream(inputStream)
            if (userBitMap != null) {
                binding.ivTarget.setImageBitmap(userBitMap)
            } else {
                Log.d("MainAc", "Loi load target image")
            }
        } catch (e: Exception) {
            Log.d("MainAc", "Loi load target image: ${e.message} - ${e.printStackTrace()}")
        }
    }

    private fun loadUserImage(imageUri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(imageUri)
            val userBitMap = BitmapFactory.decodeStream(inputStream)
            if (userBitMap != null) {
                binding.ivUser.setImageBitmap(userBitMap)
            } else {
                Log.d("MainAc", "Load to bitmap fail")
            }
        } catch (e: Exception) {
            Log.d("MainAc", "Loi load user image: ${e.message} - ${e.printStackTrace()}")
        }
    }

}