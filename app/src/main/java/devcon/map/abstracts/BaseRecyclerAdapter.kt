package devcon.map.abstracts

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<E:Any,VH: RecyclerView.ViewHolder>(
    diffUtil: DiffUtil.ItemCallback<E>
) : ListAdapter<E, VH>(diffUtil){

}