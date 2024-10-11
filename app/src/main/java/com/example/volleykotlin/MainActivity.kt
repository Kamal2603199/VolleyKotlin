package com.example.volleykotlin

import android.app.Activity
import android.app.ProgressDialog
import android.app.ProgressDialog.show
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.volleykotlin.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val url:String="https://meme-api.com/gimme"
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getMemeData()
        binding.btnNewMeme.setOnClickListener { getMemeData() }

    }

    fun getMemeData() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait while data is fetch")
        progressDialog.show()
// ...

// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)

// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Display the first 500 characters of the response string.
                Log.e("Response","getMemeData: " + response.toString())
                var responseObject=JSONObject(response)
                responseObject.get("url")
                responseObject.get("postLink")
                binding.memeTitle.text = responseObject.getString("title")
                binding.memeAuthor.text=responseObject.getString("author")
                Glide.with(this).load(responseObject.get("url")).into(binding.memeImage)
                progressDialog.dismiss()
            }
        ){
            error->
            progressDialog.dismiss()
              Toast.makeText(this@MainActivity,"${error.localizedMessage}",Toast.LENGTH_SHORT).show()
        }
        queue.add(stringRequest)
    }

}