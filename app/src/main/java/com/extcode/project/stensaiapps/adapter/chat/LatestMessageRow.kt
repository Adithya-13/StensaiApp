package com.extcode.project.stensaiapps.adapter.chat

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.ChatMessage
import com.extcode.project.stensaiapps.model.StudentModel
import com.extcode.project.stensaiapps.model.TeacherModel
import com.extcode.project.stensaiapps.other.kIdStatus
import com.extcode.project.stensaiapps.screens.activity.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_messages_user_item.view.*

class LatestMessageRow(private var chatMessage: ChatMessage) : Item<GroupieViewHolder>() {

    var studentModel: StudentModel? = null
    var teacherModel: TeacherModel? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val idStatus =
            viewHolder.itemView.context.getSharedPreferences(
                SignInActivity::class.simpleName,
                AppCompatActivity.MODE_PRIVATE
            ).getInt(
                kIdStatus,
                0
            )

        val status = if (idStatus == 0) "teachers" else "students"

        val partnerId =
            if (chatMessage.fromId == FirebaseAuth.getInstance().uid) chatMessage.toId else chatMessage.fromId

        val ref = FirebaseDatabase.getInstance().getReference("/users/$status/$partnerId")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (idStatus == 0) {
                    teacherModel = snapshot.getValue(TeacherModel::class.java)
                    viewHolder.itemView.latestMessageName.text = teacherModel?.username
                } else {
                    studentModel = snapshot.getValue(StudentModel::class.java)
                    val name = "${studentModel?.username} - ${studentModel?.className}"
                    viewHolder.itemView.latestMessageName.text = name
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("anjay", error.toString())
            }
        })

        viewHolder.itemView.latestMessageText.text = chatMessage.text
    }

    override fun getLayout(): Int {
        return R.layout.latest_messages_user_item
    }
}