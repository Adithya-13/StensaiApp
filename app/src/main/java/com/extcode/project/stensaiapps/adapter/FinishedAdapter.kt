package com.extcode.project.stensaiapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.db.FinishedData
import com.extcode.project.stensaiapps.other.OnFinishedItemClickCallback
import kotlinx.android.synthetic.main.task_fragment_list_item.view.*

class FinishedAdapter : RecyclerView.Adapter<FinishedAdapter.ViewHolder>() {

    var finishedList = ArrayList<FinishedData>()
        set(value) {
            this.finishedList.clear()
            this.finishedList.addAll(value)
            notifyDataSetChanged()
        }

    private var onFinishedItemClickCallback: OnFinishedItemClickCallback? = null

    fun setOnFinishedItemClickCallback(onFinishedItemClickCallback: OnFinishedItemClickCallback) {
        this.onFinishedItemClickCallback = onFinishedItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.task_fragment_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(finishedList[position])
    }

    override fun getItemCount(): Int = finishedList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(finishedData: FinishedData) {
            with(itemView) {
                fragmentTitleTask.text = finishedData.title
                fragmentDescriptionTask.text = finishedData.description

                itemView.setOnClickListener {
                    onFinishedItemClickCallback?.sendFinishedData(
                        finishedData
                    )
                }

                val backgroundPriority = when (finishedData.priority) {
                    0 -> R.color.white
                    1 -> R.drawable.green_priority
                    2 -> R.drawable.yellow_priority
                    3 -> R.drawable.red_priority
                    else -> R.color.white
                }

                priorityLevel.setImageResource(backgroundPriority)

                if (finishedData.date.equals("null") && finishedData.time.equals("null")) {
                    dlDateTask.visibility = View.GONE
                    dlTimeTask.visibility = View.GONE
                    return
                }
                dlTimeTask.visibility = View.VISIBLE
                dlDateTask.visibility = View.VISIBLE
                dlDateTask.text = finishedData.date
                dlTimeTask.text = finishedData.time

            }
        }
    }
}