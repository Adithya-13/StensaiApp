package com.extcode.project.stensaiapps.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.db.ScheduleData
import com.extcode.project.stensaiapps.other.kIsEditSchedule
import com.extcode.project.stensaiapps.other.kScheduleData
import com.extcode.project.stensaiapps.screens.activity.AddScheduleActivity
import kotlinx.android.synthetic.main.task_list_item.view.*

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    var scheduleLists = ArrayList<ScheduleData>()
        set(value) {
            this.scheduleLists.clear()
            this.scheduleLists.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(scheduleLists[position])
    }

    override fun getItemCount(): Int = scheduleLists.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(scheduleData: ScheduleData) {
            with(itemView) {
                titleTask.text = scheduleData.title
                descriptionTask.text = scheduleData.description
                setOnClickListener {
                    context.startActivity(Intent(context, AddScheduleActivity::class.java).apply {
                        putExtra(kScheduleData, scheduleData)
                        putExtra(kIsEditSchedule, true)
                    })
                }
            }
        }
    }
}