package com.example.firebaseimagelablingproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.firebaseimagelablingproject.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.IOException

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val MY_ML_KEY: String = "hmMlKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//!        disabling night mode of activity this will impact on fragments as well
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        
        binding.galleryImageView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivityForResult(Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                }, 112)
            }
        })

    }

//!    catch camera and gallery intent data here
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 112) {
            val image: InputImage
            try {
                image = InputImage.fromFilePath(this@MainActivity, data?.data!!)
                val imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
                imageLabeler.process(image).addOnSuccessListener { labels ->
                    for (label in labels) {
                        Log.d(MY_ML_KEY, "confidence: ${label.confidence} - ${label.text}")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d(MY_ML_KEY, "onActivityResult: ${e.message}")
                Toast.makeText(this@MainActivity, "error check internet connection!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}