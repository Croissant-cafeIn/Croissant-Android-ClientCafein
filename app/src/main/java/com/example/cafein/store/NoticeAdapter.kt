package com.example.cafein.store

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafein.R
import kotlinx.android.synthetic.main.recyclerview_notice.view.*
import java.util.HashSet

/*class NoticeAdapter(
    private val items: List<Notice>,
    private val clickListener: (notice: Notice) -> Unit) :

    RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {
    private val expandedPositionSet: HashSet<Int> = HashSet()
    class NoticeViewHolder(val binding: ItemNoticeBinding): RecyclerView.ViewHolder(binding.root){
        //var storeImage: ImageView? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notice, parent, false)
        val viewHolder = NoticeViewHolder(ItemNoticeBinding.bind(view))
        view.setOnClickListener{
            clickListener.invoke(items[viewHolder.adapterPosition])
        }
        //viewHolder.storeImage = viewHolder.itemView.findViewById<ImageView>(R.id.store_image)

        return viewHolder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.binding.notice = items[position]
        holder.itemView.expand_layout.setOnExpandListener(object :
            ExpandableLayout.OnExpandListener {
            override fun onExpand(expanded: Boolean) {
                if (expandedPositionSet.contains(position)) {
                    expandedPositionSet.remove(position)
                } else {
                    expandedPositionSet.add(position)
                }
            }
        })
        holder.itemView.expand_layout.setExpand(expandedPositionSet.contains(position))
        //Glide.with(holder.itemView.context).load(items[position].storeUrl).into(holder.storeImage!!)

    }

}*/

class NoticeAdapter(
    private val itemsCells: List<Notice>,
    function: (Nothing) -> Unit
) :
    RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    // Save the expanded row position
    private val expandedPositionSet: HashSet<Int> = HashSet()
    lateinit var context: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_notice, parent, false)
        val vh = ViewHolder(v)
        context = parent.context
        return vh
    }

    override fun getItemCount(): Int {
        return itemsCells.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Add data to cells
        //holder.itemView.question_textview.text = itemsCells[position].id.toString()
        holder.itemView.question_textview.text = itemsCells[position].title
        holder.itemView.date_layout.text = itemsCells[position].date
        holder.itemView.answer_textview.text = itemsCells[position].text

        // Expand when you click on cell
        holder.itemView.expand_layout.setOnExpandListener(object :
            ExpandableLayout.OnExpandListener {
            override fun onExpand(expanded: Boolean) {
                if (expandedPositionSet.contains(position)) {
                    expandedPositionSet.remove(position)
                } else {
                    expandedPositionSet.add(position)
                }
            }
        })
        holder.itemView.expand_layout.setExpand(expandedPositionSet.contains(position))
    }
}