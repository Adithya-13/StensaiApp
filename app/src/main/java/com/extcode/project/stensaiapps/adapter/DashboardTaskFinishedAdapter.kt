package com.extcode.project.stensaiapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.db.FinishedData
import kotlinx.android.synthetic.main.task_list_item.view.*

class DashboardTaskFinishedAdapter :
    RecyclerView.Adapter<DashboardTaskFinishedAdapter.ViewHolder>() {

    var tasksList = ArrayList<FinishedData>()
        set(value) {
            this.tasksList.clear()
            this.tasksList.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasksList[position])
    }

    override fun getItemCount(): Int = tasksList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(finishedData: FinishedData) {
            with(itemView) {
                titleTask.text = finishedData.title
                descriptionTask.text = finishedData.description
            }
        }
    }
}