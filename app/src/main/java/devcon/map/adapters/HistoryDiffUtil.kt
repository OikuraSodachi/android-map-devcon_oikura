package devcon.map.adapters

import androidx.recyclerview.widget.DiffUtil
import devcon.map.restapi.KeywordDocument

class HistoryDiffUtil : DiffUtil.ItemCallback<KeywordDocument>() {
    override fun areItemsTheSame(oldItem: KeywordDocument, newItem: KeywordDocument): Boolean {
        return (oldItem.address_name == newItem.address_name) && (oldItem.place_name == newItem.place_name)
    }

    override fun areContentsTheSame(oldItem: KeywordDocument, newItem: KeywordDocument): Boolean {
        return oldItem == newItem
    }
}