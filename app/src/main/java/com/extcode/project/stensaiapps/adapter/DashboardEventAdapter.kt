package com.extcode.project.stensaiapps.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.api.EventItem
import com.extcode.project.stensaiapps.other.kDetailEvent
import com.extcode.project.stensaiapps.screens.activity.DetailMagazineActivity
import kotlinx.android.synthetic.main.event_list_item.view.*

class DashboardEventAdapter : RecyclerView.Adapter<DashboardEventAdapter.ViewHolder>() {

    var eventList = ArrayList<EventItem>()
        set(value) {
            this.eventList.clear()
            this.eventList.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.event_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(eventList[position])
    }

    override fun getItemCount(): Int = eventList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(eventItem: EventItem) {
            with(itemView) {
                titleEvent.text = eventItem.nama

                Glide.with(itemView.context)
                    .load("http://stensai-apps.com/img/event/${eventItem.foto}")
                    .placeholder(R.drawable.ic_loading)
                    .into(pictureEvent)

                setOnClickListener {
                    context.startActivity(Intent(context, DetailMagazineActivity::class.java).apply {
                        putExtra(kDetailEvent, eventItem)
                    })
                }
            }
        }
    }
}