package com.kolosov.congratulations.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import com.kolosov.congratulations.R
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(
    var names: List<String>?,
    var date: String?,
    var bigDate: String?,
    var context: Context
) : SliderViewAdapter<SliderAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.slider_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(
        viewHolder: Holder,
        position: Int
    ) {
        if (names?.get(position)?.length!! > 55) {
            viewHolder.name.textSize = 18f
        }
        viewHolder.name.text = names!![position]
        viewHolder.date.text = date
        viewHolder.bigDateTextView.text = bigDate
        viewHolder.foundBlackButton.setOnClickListener{ v: View? ->
          v?.findNavController()?.navigate(R.id.searchFragment)
        }
    }

    override fun getCount(): Int {
        return names!!.size
    }

    class Holder(itemView: View) : ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.name)
        var date: TextView = itemView.findViewById(R.id.date)
        var bigDateTextView: TextView = itemView.findViewById(R.id.bigDateTextView)
        var foundBlackButton: Button = itemView.findViewById(R.id.home_black_found_button)

    }
}