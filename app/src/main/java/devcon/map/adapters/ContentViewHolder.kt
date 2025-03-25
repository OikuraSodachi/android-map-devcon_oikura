package devcon.map.adapters

import android.view.View
import android.widget.TextView
import devcon.learn.contacts.R
import devcon.map.abstracts.BaseRecyclerViewHolder
import devcon.map.data.SampleData

class ContentViewHolder(itemView: View, private val onClick: (data: SampleData) -> Unit) :
    BaseRecyclerViewHolder<SampleData>(itemView) {
    //  private val contentImageView: ImageView = itemView.findViewById(R.id.contentImageView)
    private val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)

    override fun onInit(item: SampleData) {
        contentTextView.text = item.title
        itemView.setOnClickListener {
            onClick(item)
        }
    }
}