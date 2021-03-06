package com.extcode.project.stensaiapps.adapter.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.api.StudentData
import com.extcode.project.stensaiapps.other.OnStudentNewMessageItemClickCallback
import kotlinx.android.synthetic.main.user_list_item.view.*

class StudentNewMessageAdapter : RecyclerView.Adapter<StudentNewMessageAdapter.ViewHolder>() {

    var arrNewMessage = ArrayList<StudentData>()
        set(value) {
            this.arrNewMessage.clear()
            this.arrNewMessage.addAll(value)
            notifyDataSetChanged()
        }

    private var onStudentNewMessageItemClickCallback: OnStudentNewMessageItemClickCallback? = null

    fun setOnStudentNewMessageItemClickCallback(onStudentNewMessageItemClickCallback: OnStudentNewMessageItemClickCallback) {
        this.onStudentNewMessageItemClickCallback = onStudentNewMessageItemClickCallback
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
        fun bind(studentData: StudentData) {
            with(itemView) {
                val name = studentData.nama
                newMessageName.text = name
                itemView.setOnClickListener {
                    onStudentNewMessageItemClickCallback?.sendStudentData(
                        studentData
                    )
                }
            }
        }
    }
}