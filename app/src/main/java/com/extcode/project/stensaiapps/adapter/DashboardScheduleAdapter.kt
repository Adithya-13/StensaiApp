package com.extcode.project.stensaiapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.db.ScheduleData
import kotlinx.android.synthetic.main.task_list_item.view.*

class DashboardScheduleAdapter : RecyclerView.Adapter<DashboardScheduleAdapter.ViewHolder>() {

    var scheduleList = ArrayList<ScheduleData>()
        set(value) {
            this.scheduleList.clear()
            this.scheduleList.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(scheduleList[position])
    }

    override fun getItemCount(): Int = scheduleList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(scheduleData: ScheduleData) {
            with(itemView) {
                titleTask.text = scheduleData.title
                descriptionTask.text = scheduleData.description
            }
        }
    }
}