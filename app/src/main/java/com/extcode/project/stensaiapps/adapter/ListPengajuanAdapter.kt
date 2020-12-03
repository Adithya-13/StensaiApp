package com.extcode.project.stensaiapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.api.PengajuanItem
import kotlinx.android.synthetic.main.peran_list_item.view.*

class ListPengajuanAdapter : RecyclerView.Adapter<ListPengajuanAdapter.ViewHolder>() {

    var pengajuanList = ArrayList<PengajuanItem>()
        set(value) {
            this.pengajuanList.clear()
            this.pengajuanList.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.peran_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pengajuanList[position])
    }

    override fun getItemCount(): Int = pengajuanList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pengajuanItem: PengajuanItem) {
            with(itemView) {
                studentPeran.text = pengajuanItem.nama
                fragmentTitlePeran.text = pengajuanItem.pengajuan
                fragmentDescriptionPeran.text = pengajuanItem.deskripsi
            }
        }
    }
}