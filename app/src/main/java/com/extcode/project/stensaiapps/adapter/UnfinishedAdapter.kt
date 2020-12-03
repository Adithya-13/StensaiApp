package com.extcode.project.stensaiapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.db.UnfinishedData
import com.extcode.project.stensaiapps.other.OnUnfinishedItemClickCallback
import kotlinx.android.synthetic.main.task_fragment_list_item.view.*

class UnfinishedAdapter : RecyclerView.Adapter<UnfinishedAdapter.ViewHolder>() {

    var unfinishedList = ArrayList<UnfinishedData>()
        set(value) {
            this.unfinishedList.clear()
            this.unfinishedList.addAll(value)
            notifyDataSetChanged()
        }

    private var onUnfinishedItemClickCallback: OnUnfinishedItemClickCallback? = null

    fun setOnUnfinishedItemClickCallback(onUnfinishedItemClickCallback: OnUnfinishedItemClickCallback) {
        this.onUnfinishedItemClickCallback = onUnfinishedItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.task_fragment_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(unfinishedList[position])
    }

    override fun getItemCount(): Int = unfinishedList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(unfinishedData: UnfinishedData) {
            with(itemView) {
                fragmentTitleTask.text = unfinishedData.title
                fragmentDescriptionTask.text = unfinishedData.description

                itemView.setOnClickListener {
                    onUnfinishedItemClickCallback?.sendUnfinishedData(
                        unfinishedData
                    )
                }

                val backgroundPriority = when (unfinishedData.priority) {
                    0 -> R.color.white
                    1 -> R.drawable.green_priority
                    2 -> R.drawable.yellow_priority
                    3 -> R.drawable.red_priority
                    else -> R.color.white
                }

                priorityLevel.setImageResource(backgroundPriority)

                if (unfinishedData.date.equals("null") && unfinishedData.time.equals("null")) {
                    dlDateTask.visibility = View.GONE
                    dlTimeTask.visibility = View.GONE
                    return
                }
                dlTimeTask.visibility = View.VISIBLE
                dlDateTask.visibility = View.VISIBLE
                dlDateTask.text = unfinishedData.date
                dlTimeTask.text = unfinishedData.time
            }
        }
    }
}