package com.example.firebaseimagelablingproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.round

class CustomAdapter(var modelList: ArrayList<model>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_processing_result_recycler_view, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = modelList.get(position)
        holder.nameTextView.text = model.name
        val finalConfidence = model.confidence * 100
        holder.confidenceTextView.text = "${finalConfidence}%"
    }

    public fun updateAdapter (updatedModelList: ArrayList<model>) {
        modelList.addAll(updatedModelList)
        notifyDataSetChanged()
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return modelList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nameTextView = ItemView.findViewById<TextView>(R.id.nameTextView)
        val confidenceTextView = ItemView.findViewById<TextView>(R.id.confidenceTextView)
    }
}
