package com.example.firebaseimagelablingproject

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseimagelablingproject.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.IOException

class MainActivity : AppCompatActivity() {

    lateinit var customAdapter: CustomAdapter
    lateinit var binding: ActivityMainBinding

    val MY_ML_KEY: String = "hmMlKey"
    val modelList = ArrayList<model>()

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
                }, 111)
            }
        })

        binding.cameraImageView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivityForResult(Intent().apply {
                    action = android.provider.MediaStore.ACTION_IMAGE_CAPTURE
                }, 112)
            }
        })

        customAdapter = CustomAdapter(modelList)

        // this creates a vertical layout Manager
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        // Setting the Adapter with the recyclerview
        binding.recyclerView.adapter = customAdapter
    }

//!    catch camera and gallery intent data here
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        modelList.clear()
        if (requestCode == 111) {
            val image: InputImage
            binding.roundedImageView.setImageURI(data?.data)
            try {
                image = InputImage.fromFilePath(this@MainActivity, data?.data!!)
                val imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
                imageLabeler.process(image).addOnSuccessListener { labels ->
                    for (label in labels) {
                        modelList.add(model(label.text, label.confidence))
                    }
                    modelList.size
                    customAdapter.updateAdapter(modelList)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d(MY_ML_KEY, "onActivityResult: ${e.message}")
                Toast.makeText(this@MainActivity, "error check internet connection!", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == 112) {
            val bitmapImage: Bitmap = data?.extras?.get("data") as Bitmap
            binding.roundedImageView.setImageBitmap(bitmapImage)
            val image = InputImage.fromBitmap(bitmapImage, 0)
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    for (label in labels) {
                        modelList.add(model(label.text, label.confidence))
                    }
                    modelList.size
                    customAdapter.updateAdapter(modelList)
                }
                .addOnFailureListener { e ->
                }
        }

    }

}