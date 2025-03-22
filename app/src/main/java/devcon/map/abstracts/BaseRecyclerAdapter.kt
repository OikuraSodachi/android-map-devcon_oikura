package devcon.map.abstracts

import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow

abstract class BaseRecyclerAdapter<E:Any,VH:RecyclerView.ViewHolder>(
    itemFlow: Flow<List<E>>,
): RecyclerView.Adapter<VH>() {

    /** Todo: 데이터 갱신 로직 개선 필요해 보임 **/
    open val observer = Observer<List<E>> {
        itemList = it
        notifyDataSetChanged()
    }

    private val itemLiveData = itemFlow.asLiveData()
    var itemList = emptyList<E>()

    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        itemLiveData.observeForever(observer)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        itemLiveData.removeObserver(observer)
    }

}