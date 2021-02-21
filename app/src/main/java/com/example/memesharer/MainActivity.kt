package com.example.memesharer

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Calendar.getInstance
import java.util.Currency.getInstance

class MainActivity : AppCompatActivity() {

    var currentimageurl:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadmeme()
    }

    private fun loadmeme() {
        progressbar1.visibility=View.VISIBLE
//        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                currentimageurl = response.getString("url")
                Glide.with(this).load(currentimageurl).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressbar1.visibility=View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressbar1.visibility=View.GONE
                        return false
                    }
                }).into(memeimage)
            },
            Response.ErrorListener {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            })

        // Add the request to the RequestQueue.
//        queue.add(jsonObjectRequest)
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun nextmeme(view: View) {
        loadmeme()
    }
    fun sharememe(view: View) {
        val intent= Intent(Intent.ACTION_SEND)
        intent.type= "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey, Check this meme $currentimageurl")
        val chooser = Intent.createChooser(intent,"Share this using..")
        startActivity(chooser)
    }
}

