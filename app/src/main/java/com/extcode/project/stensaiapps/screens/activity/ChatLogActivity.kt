package com.extcode.project.stensaiapps.screens.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.adapter.chat.ChatLeftItem
import com.extcode.project.stensaiapps.adapter.chat.ChatRightItem
import com.extcode.project.stensaiapps.model.ChatMessage
import com.extcode.project.stensaiapps.model.api.StudentData
import com.extcode.project.stensaiapps.model.api.TeacherData
import com.extcode.project.stensaiapps.other.kIdStatus
import com.extcode.project.stensaiapps.other.kUid
import com.extcode.project.stensaiapps.other.kUserData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    private lateinit var teacherData: TeacherData
    private lateinit var studentData: StudentData
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        rvChatLog.layoutManager = LinearLayoutManager(this)
        rvChatLog.adapter = adapter

        val idStatus = getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).getInt(
            kIdStatus,
            0
        )

        val toIdStatus = if (idStatus == 0) 1 else 0

        configTitleBar(idStatus)

        listenForMessages(idStatus, toIdStatus)

        fabSendMessage.setOnClickListener {
            performSendMessage(idStatus, toIdStatus)
        }

        editTextMessage.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) rvChatLog.scrollToPosition(adapter.itemCount - 1)
            }
    }

    private fun listenForMessages(idStatus: Int, toIdStatus: Int) {

        if (idStatus == 0) {
            teacherData = intent.getParcelableExtra(kUserData)!!
        } else {
            studentData = intent.getParcelableExtra(kUserData)!!
        }

        val fromId = getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).getInt(
            kUid, 0
        ).toString()
        val toId = if (idStatus == 0) teacherData.id else studentData.id

        val ref = FirebaseDatabase.getInstance()
            .getReference("/user-messages/$fromId-$idStatus/$toId-$toIdStatus")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("anjay", snapshot.toString())
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    if (chatMessage.fromId == fromId && chatMessage.status == idStatus.toString() && chatMessage.toIdStatus == toIdStatus.toString()) {
                        adapter.add(ChatRightItem(chatMessage.text))
                    } else {
                        adapter.add(ChatLeftItem(chatMessage.text))
                    }
                }

                rvChatLog.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun performSendMessage(idStatus: Int, toIdStatus: Int) {
        val text = editTextMessage.text.toString().trim()
        editTextMessage.text.clear()
        if (text.isEmpty()) return

        if (idStatus == 0) {
            teacherData = intent.getParcelableExtra(kUserData)!!
        } else {
            studentData = intent.getParcelableExtra(kUserData)!!
        }

        val fromId = getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).getInt(
            kUid, 0
        ).toString()
        val toId = (if (idStatus == 0) teacherData.id else studentData.id).toString()

        if (fromId.isEmpty()) return

        val reference =
            FirebaseDatabase.getInstance()
                .getReference("/user-messages/$fromId-$idStatus/$toId-$toIdStatus").push()

        val toUserReference =
            FirebaseDatabase.getInstance()
                .getReference("/user-messages/$toId-$toIdStatus/$fromId-$idStatus").push()

        val latestMessageReference =
            FirebaseDatabase.getInstance()
                .getReference("/latest-messages/$fromId-$idStatus/$toId-$toIdStatus")

        val toUserLatestMessageReference =
            FirebaseDatabase.getInstance()
                .getReference("/latest-messages/$toId-$toIdStatus/$fromId-$idStatus")

        val chatMessage = ChatMessage(
            reference.key!!,
            text,
            fromId,
            toId,
            idStatus.toString(),
            toIdStatus.toString(),
            System.currentTimeMillis() / 1000
        )

        reference.setValue(chatMessage).addOnSuccessListener {
            rvChatLog.scrollToPosition(adapter.itemCount - 1)
        }
        toUserReference.setValue(chatMessage)
        latestMessageReference.setValue(chatMessage)
        toUserLatestMessageReference.setValue(chatMessage)
    }

    private fun configTitleBar(idStatus: Int) {
        if (idStatus == 0) {
            teacherData = intent.getParcelableExtra(kUserData)!!
        } else {
            studentData = intent.getParcelableExtra(kUserData)!!
        }

        setSupportActionBar(chatLogTopAppBar)
        chatLogTopAppBar.title =
            if (idStatus == 0) teacherData.nama else studentData.nama
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
