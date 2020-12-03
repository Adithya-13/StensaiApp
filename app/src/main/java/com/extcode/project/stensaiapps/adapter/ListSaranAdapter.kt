package com.extcode.project.stensaiapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.api.AllStudentItem
import com.extcode.project.stensaiapps.model.api.SaranItem
import kotlinx.android.synthetic.main.peran_list_item.view.*

class ListSaranAdapter : RecyclerView.Adapter<ListSaranAdapter.ViewHolder>() {

    var saranList = ArrayList<SaranItem>()
        set(value) {
            this.saranList.clear()
            this.saranList.addAll(value)
            notifyDataSetChanged()
        }

    var siswaIdList = ArrayList<AllStudentItem>()
        set(value) {
            this.siswaIdList.clear()
            this.siswaIdList.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.peran_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(saranList[position])
    }

    override fun getItemCount(): Int = saranList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(saranItem: SaranItem) {
            with(itemView) {
                for (siswa in siswaIdList) {
                    if (siswa.id.toString() == saranItem.siswaId) {
                        studentPeran.text = siswa.nama
                    }
                }
                fragmentTitlePeran.text = saranItem.nama
                fragmentDescriptionPeran.text = saranItem.deskripsi
            }
        }
    }
}