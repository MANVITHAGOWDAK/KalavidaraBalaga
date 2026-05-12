package com.example.kalavidarabalaga

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ArtistAdapter(
    private var list: ArrayList<Artist>,
    private val onItemClick: (Artist) -> Unit
) : RecyclerView.Adapter<ArtistAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val art: TextView = itemView.findViewById(R.id.art)
        val location: TextView = itemView.findViewById(R.id.location)
        val image: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = list[position]

        holder.name.text = artist.name
        holder.art.text = artist.art
        holder.location.text = artist.location

        Glide.with(holder.itemView.context)
            .load(artist.imageUrl)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            onItemClick(artist)
        }
    }

    override fun getItemCount(): Int = list.size
}