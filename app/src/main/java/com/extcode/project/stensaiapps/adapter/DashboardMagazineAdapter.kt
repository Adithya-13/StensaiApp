package com.extcode.project.stensaiapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.MagazineModel
import kotlinx.android.synthetic.main.magazine_list_item.view.*

class DashboardMagazineAdapter : RecyclerView.Adapter<DashboardMagazineAdapter.ViewHolder>() {

    var magazinesList = ArrayList<MagazineModel>()
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
        fun bind(magazineModel: MagazineModel) {
            with(itemView) {
                titleMagazine.text = context.getString(R.string.uploadedBy, magazineModel.title)
                Glide.with(itemView.context)
                    .load(magazineModel.avatar)
                    .into(avatarMagazine)
                Glide.with(itemView.context)
                    .load(magazineModel.picture)
                    .into(pictureMagazine)
            }
        }
    }
}