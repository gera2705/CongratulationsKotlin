package com.kolosov.congratulations.ui.favourites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kolosov.congratulations.R

class FavoriteRecyclerAdapter(private val arrayList: List<String>, private val context: Context) :
    RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoritesText = arrayList[position]
        holder.cardTextView.text = favoritesText
        holder.cardTextView.setOnClickListener { v: View? ->
            val action: FavouritesFragmentDirections.ActionFavouritesFragmentToFavoritesTextFragment =
                FavouritesFragmentDirections.actionFavouritesFragmentToFavoritesTextFragment(favoritesText)
            v?.findNavController()?.navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardTextView: TextView = itemView.findViewById(R.id.card_text_view)

    }
}