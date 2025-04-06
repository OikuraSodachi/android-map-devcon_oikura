package devcon.map.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import devcon.learn.contacts.R
import devcon.map.abstracts.BaseRecyclerAdapter
import devcon.map.restapi.KeywordDocument

class ContentRecyclerAdapter(
    diffUtil:DiffUtil.ItemCallback<KeywordDocument>,
    private val onClick: (data: KeywordDocument) -> Unit
) : BaseRecyclerAdapter<KeywordDocument, ContentViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_recycler_holder, parent, false)
        return ContentViewHolder(view, { onClick(it) })
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.onInit(currentList[position])
    }

}