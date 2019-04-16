package me.cpele.hustle.app

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataPointViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: Long?) {
        val textView = itemView as? TextView
        textView?.text = item.toString()
    }
}
