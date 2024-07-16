package com.example.notesappkotlin.Activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappkotlin.R

class NotesAdapterActivity(private val context: Context,private val notelist: List<NoteBeanClass>): RecyclerView.Adapter<NotesAdapterActivity.MyViewHolder>() {
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapterActivity.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_items, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesAdapterActivity.MyViewHolder, position: Int) {
        val noteBean = notelist[position]
        holder.title.text = noteBean.title
        holder.des.text = noteBean.des

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
       return notelist.size
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.title_tv)
        val des: TextView = itemView.findViewById(R.id.description_tv)
    }
}