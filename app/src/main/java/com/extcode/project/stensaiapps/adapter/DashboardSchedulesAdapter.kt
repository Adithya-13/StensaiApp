package com.extcode.project.stensaiapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.stensaiapps.R
import kotlinx.android.synthetic.main.schedule_list_item.view.*

class DashboardSchedulesAdapter : RecyclerView.Adapter<DashboardSchedulesAdapter.ViewHolder>() {

    var schedulesList = ArrayList<String>()
        set(value) {
            this.schedulesList.clear()
            this.schedulesList.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.schedule_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(schedulesList[position])
    }

    override fun getItemCount(): Int = schedulesList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(scheduleItem : String){
            with(itemView){
                schedule.text = scheduleItem
            }
        }
    }
}