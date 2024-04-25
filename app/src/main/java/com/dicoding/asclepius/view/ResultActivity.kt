package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.data.local.ResultEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var result: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        result = intent.getStringExtra(EXTRA_RESULT) ?: ""
        showResult(result)

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }

        binding.saveButton.setOnClickListener {
            val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI) ?: ""
            saveToHistory(result, imageUri)
        }
    }

    private fun showResult(result: String) {
        binding.resultText.text = result
    }

    private fun saveToHistory(result: String, imageUri: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val resultDao = History.getInstance(application).resultDao()
            val resultEntity = ResultEntity(result, imageUri)
            resultDao.insert(resultEntity)

        }
        Toast.makeText(this, "Result Saved!", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

}