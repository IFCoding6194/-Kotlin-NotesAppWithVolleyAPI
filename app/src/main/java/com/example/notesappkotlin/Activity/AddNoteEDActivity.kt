package com.example.notesappkotlin.Activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.notesappkotlin.R
import com.example.notesappkotlin.databinding.ActivityAddNoteEdactivityBinding
import org.json.JSONObject

class AddNoteEDActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteEdactivityBinding
    private lateinit var requestQueue: RequestQueue
    private var screenType: String? = null
    private var id:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteEdactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestQueue = Volley.newRequestQueue(this)

        //Set Status Bar Color
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = resources.getColor(android.R.color.white)

        intent.extras?.let {
            screenType = it.getString("screen_type")
            screenType?.let { screenType
            binding.titleTv.text = screenType
            }
            if (screenType != "Add New Notes"){
                binding.titleEdt.setText(it.getString("title"))
                binding.desEdt.setText(it.getString("description"))
                id = it.getString("id")
                binding.deleteImg.visibility = View.VISIBLE
            }
        }

        binding.backImg.setOnClickListener {
            onBackPressed()
        }

        binding.saveImg.setOnClickListener { addNotes() }

        binding.deleteImg.setOnClickListener { deleteNotes() }
    }
    private fun deleteNotes(){
        binding.progressBar.visibility = View.VISIBLE
        val url = "https://node-api-408s.onrender.com/api/product/$id"
        try {
            val jsonObjectRequest = JsonObjectRequest(Request.Method.DELETE,url, null, Response.Listener { response ->
                try {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@AddNoteEDActivity, response.getString("msg"),Toast.LENGTH_SHORT).show()
                    onBackPressed()
                    finish()
                }catch (e:Exception){
                    binding.progressBar.visibility = View.GONE
                    println("deleteNotes response e : $e")
                }
            }, Response.ErrorListener { error ->
                binding.progressBar.visibility = View.GONE
                println("deleteNotes error: $error")
            })
            requestQueue.add(jsonObjectRequest)
        }catch (e:Exception){
            binding.progressBar.visibility = View.GONE
            println("deleteNotes e : $e")
        }
    }

    private fun addNotes() {
        val title = binding.titleEdt.text.toString().trim()
        val des = binding.desEdt.text.toString().trim()

        if (TextUtils.isEmpty(title)) {
            binding.titleEdt.error = "Enter a Title"
            binding.titleEdt.requestFocus()
            return
        }
        if (TextUtils.isEmpty(des)) {
            binding.desEdt.error = "Enter a Description"
            binding.desEdt.requestFocus()
            return
        }
        binding.progressBar.visibility = View.VISIBLE
        if (screenType == "Edit & Delete Notes") {
            val url = "https://node-api-408s.onrender.com/api/product/$id"
            val jsonObject = JSONObject().apply {
                put("tittle",title)
                put("description",des)
            }
            try {
                val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, url, jsonObject,Response.Listener { response ->
                    try {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@AddNoteEDActivity,response.getString("msg"),Toast.LENGTH_SHORT).show()
                        onBackPressed()
                        finish()
                    }catch (e:Exception){
                        binding.progressBar.visibility = View.GONE
                        println("addNotes Response Edit e : $e")
                    }
                },Response.ErrorListener { error ->
                    binding.progressBar.visibility = View.GONE
                    println("addNotes Edit error : $error")
                })
                requestQueue.add(jsonObjectRequest)
            }catch (e:Exception){
                binding.progressBar.visibility = View.GONE
                println("addNotes Edit e : $e")
            }
        } else {
            binding.progressBar.visibility = View.VISIBLE
            val url = "https://node-api-408s.onrender.com/api/product"
            val jsonObject = JSONObject().apply {
                put("tittle", title)
                put("description", des)
            }
            try {
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonObject,
                    Response.Listener { response ->
                        try {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@AddNoteEDActivity,
                                response.getString("msg"),
                                Toast.LENGTH_SHORT
                            ).show()
                            onBackPressed()
                            finish()
                        } catch (e: Exception) {
                            binding.progressBar.visibility = View.GONE
                            println("addNotes Response e : $e")
                        }
                    },
                    Response.ErrorListener { error ->
                        binding.progressBar.visibility = View.GONE
                        println("addNotes Error : $error")
                    })
                requestQueue.add(jsonObjectRequest)
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                println("addNotes e : $e")
            }
        }
    }
}