package com.extcode.project.stensaiapps.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.api.MessageItem
import com.extcode.project.stensaiapps.other.kDetailMagazine
import com.extcode.project.stensaiapps.screens.activity.DetailMagazineActivity
import kotlinx.android.synthetic.main.magazine_list_item.view.*

class MagazineAdapter : RecyclerView.Adapter<MagazineAdapter.ViewHolder>() {

    var magazinesList = ArrayList<MessageItem>()
        set(value) {
            this.magazinesList.clear()
            this.magazinesList.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.magazine_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(magazinesList[position])
    }

    override fun getItemCount(): Int = magazinesList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messageItem: MessageItem) {
            with(itemView) {
                adminMagazine.text = context.getString(R.string.uploadedBy, messageItem.name)
                titleMagazine.text = messageItem.judul

                Glide.with(itemView.context)
                    .load(R.drawable.ic_profile_user)
                    .into(avatarMagazine)
                Glide.with(itemView.context)
                    .load("http://stensai-apps.com/img/thumbnail/${messageItem.thumbnail}")
                    .placeholder(R.drawable.ic_loading)
                    .into(pictureMagazine)

                setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            DetailMagazineActivity::class.java
                        ).apply {
                            putExtra(kDetailMagazine, messageItem)
                        })
                }
            }
        }
    }
}