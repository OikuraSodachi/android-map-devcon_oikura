package devcon.map.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import devcon.learn.contacts.R
import devcon.map.data.SampleData

class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //  private val contentImageView: ImageView = itemView.findViewById(R.id.contentImageView)
    private val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)

    fun onInit(data: SampleData, onClick: (data: SampleData) -> Unit) {
        contentTextView.text = data.title
        itemView.setOnClickListener {
            onClick(data)
        }
    }
}