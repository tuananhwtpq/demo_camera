package com.example.baseproject.activities

import android.net.Uri
import android.view.WindowManager
import android.widget.Toast
import com.example.baseproject.R
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityMainBinding
import org.opencv.android.OpenCVLoader

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private var imageUri: String? = null

    override fun initData() {
        makeStatusBarTransparent()
        testOpenCV()
    }

    override fun initView() {
        imageUri = intent.getStringExtra("image")
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

}