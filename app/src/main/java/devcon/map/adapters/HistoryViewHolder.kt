package devcon.map.adapters

import android.view.View
import android.widget.TextView
import devcon.learn.contacts.R
import devcon.map.abstracts.BaseRecyclerViewHolder
import devcon.map.data.SampleData

class HistoryViewHolder(itemView: View, private val onClick: (id: Long) -> Unit) :
    BaseRecyclerViewHolder<SampleData>(itemView) {
    private val historyTextView: TextView = itemView.findViewById(R.id.historyTextView)

    override fun onInit(item: SampleData) {
        historyTextView.text = item.title
        itemView.setOnClickListener {
            onClick(item.id)
        }
    }
}