package com.extcode.project.stensaiapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.db.UnfinishedData
import kotlinx.android.synthetic.main.task_list_item.view.*

class DashboardTaskAdapter : RecyclerView.Adapter<DashboardTaskAdapter.ViewHolder>() {

    var tasksList = ArrayList<UnfinishedData>()
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
        fun bind(unfinishedData: UnfinishedData) {
            with(itemView) {
                titleTask.text = unfinishedData.title
                descriptionTask.text = unfinishedData.description
            }
        }
    }
}