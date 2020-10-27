package com.extcode.project.stensaiapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.TeacherModel
import com.extcode.project.stensaiapps.other.OnTeacherNewMessageItemClickCallback
import kotlinx.android.synthetic.main.user_list_item.view.*

class TeacherNewMessageAdapter : RecyclerView.Adapter<TeacherNewMessageAdapter.ViewHolder>() {

    var arrNewMessage = ArrayList<TeacherModel>()
        set(value) {
            this.arrNewMessage.clear()
            this.arrNewMessage.addAll(value)
            notifyDataSetChanged()
        }

    private var onTeacherNewMessageItemClickCallback: OnTeacherNewMessageItemClickCallback? = null

    fun setOnTeacherNewMessageItemClickCallback(onTeacherNewMessageItemClickCallback: OnTeacherNewMessageItemClickCallback) {
        this.onTeacherNewMessageItemClickCallback = onTeacherNewMessageItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrNewMessage[position])
    }

    override fun getItemCount() = arrNewMessage.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(teacherModel: TeacherModel) {
            with(itemView) {
                newMessageName.text = teacherModel.username
                itemView.setOnClickListener {
                    onTeacherNewMessageItemClickCallback?.sendTeacherData(
                        teacherModel
                    )
                }
            }
        }
    }
}