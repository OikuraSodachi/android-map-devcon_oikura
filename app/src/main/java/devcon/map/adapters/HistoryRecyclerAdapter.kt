package devcon.map.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import devcon.learn.contacts.R
import devcon.map.abstracts.BaseRecyclerAdapter
import devcon.map.restapi.KeywordDocument

class HistoryRecyclerAdapter(
    diffUtil: DiffUtil.ItemCallback<KeywordDocument>,
    private val onClick: (data: KeywordDocument) -> Unit
) : BaseRecyclerAdapter<KeywordDocument, HistoryViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_recycler_holder, parent, false)
        return HistoryViewHolder(view, { onClick(it) })
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.onInit(currentList[position])
    }

}