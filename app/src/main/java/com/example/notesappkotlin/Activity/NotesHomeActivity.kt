package com.example.notesappkotlin.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.notesappkotlin.R
import com.example.notesappkotlin.databinding.ActivityNotesHomeBinding
import org.json.JSONArray
import org.json.JSONObject

class NotesHomeActivity : AppCompatActivity(), NotesAdapterActivity.OnItemClickListener{
    private lateinit var binding: ActivityNotesHomeBinding
    private lateinit var requestQueue: RequestQueue
    private val notelist: ArrayList<NoteBeanClass> = ArrayList()
    private lateinit var notesAdapter: NotesAdapterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestQueue = Volley.newRequestQueue(this)

        // Set Status Bar Color
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = resources.getColor(android.R.color.white)

        setUpRecyclerView()

        getAllNotes()

        binding.addNotesImg.setOnClickListener {
            val intent = Intent(this@NotesHomeActivity,AddNoteEDActivity::class.java)
            intent.putExtra("screen_type","Add New Notes")
            startActivity(intent)
        }
    }

    private fun setUpRecyclerView(){
        binding.notesRcv.layoutManager = GridLayoutManager(this,1)
        notesAdapter = NotesAdapterActivity(this, notelist)
        binding.notesRcv.adapter = notesAdapter
        notesAdapter.setOnItemClickListener(this)
    }

    private fun getAllNotes(){
        notelist.clear()
        binding.progressBar.visibility = View.VISIBLE
        val url = "https://node-api-408s.onrender.com/api/product"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url, null, Response.Listener { response ->
            try {
                binding.progressBar.visibility = View.GONE
                println("getAllNotes Response : ${response.toString()}")
                val jsonArray: JSONArray = response.getJSONArray("notes")
                for (i in 0 until jsonArray.length()){
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                    val noteBeanClass = NoteBeanClass().apply {
                        title = jsonObject.getString("tittle")
                        des = jsonObject.getString("description")
                        id = jsonObject.getString("_id")
                    }
                    notelist.add(noteBeanClass)
                }
                notesAdapter.notifyDataSetChanged()
            }catch (e: Exception){
                binding.progressBar.visibility = View.GONE
                e.printStackTrace()
            }
        }, Response.ErrorListener { error ->
            binding.progressBar.visibility = View.GONE
            error.printStackTrace()
        })
        requestQueue.add(jsonObjectRequest)
    }

    override fun onItemClick(position: Int) {
       val noteBeanClass: NoteBeanClass = notelist[position]
        val intent = Intent(this@NotesHomeActivity, AddNoteEDActivity::class.java).apply {
            putExtra("title", noteBeanClass.title)
            putExtra("description",noteBeanClass.des)
            putExtra("id",noteBeanClass.id)
            putExtra("screen_type","Edit & Delete Notes")
        }
        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
        getAllNotes()
    }
}