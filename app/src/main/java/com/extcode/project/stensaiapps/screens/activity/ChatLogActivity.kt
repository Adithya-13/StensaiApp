package com.extcode.project.stensaiapps.screens.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.adapter.ChatLeftItem
import com.extcode.project.stensaiapps.adapter.ChatRightItem
import com.extcode.project.stensaiapps.model.ChatMessage
import com.extcode.project.stensaiapps.model.StudentModel
import com.extcode.project.stensaiapps.model.TeacherModel
import com.extcode.project.stensaiapps.other.kIdStatus
import com.extcode.project.stensaiapps.other.kUserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    private lateinit var teacherModel: TeacherModel
    private lateinit var studentModel: StudentModel
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
        configTitleBar(idStatus)

        listenForMessages(idStatus)

        fabSendMessage.setOnClickListener {
            performSendMessage(idStatus)
        }

        editTextMessage.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) rvChatLog.scrollToPosition(adapter.itemCount - 1)
            }
    }

    private fun listenForMessages(idStatus: Int) {

        if (idStatus == 0) {
            teacherModel = intent.getParcelableExtra(kUserData)!!
        } else {
            studentModel = intent.getParcelableExtra(kUserData)!!
        }

        val fromId = FirebaseAuth.getInstance().uid
        val toId = if (idStatus == 0) teacherModel.uid else studentModel.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("anjay", snapshot.toString())
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
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

    private fun performSendMessage(idStatus: Int) {
        val text = editTextMessage.text.toString().trim()
        if (text.isEmpty()) return

        if (idStatus == 0) {
            teacherModel = intent.getParcelableExtra(kUserData)!!
        } else {
            studentModel = intent.getParcelableExtra(kUserData)!!
        }

        val fromId = FirebaseAuth.getInstance().uid
        val toId = if (idStatus == 0) teacherModel.uid else studentModel.uid

        if (fromId.isNullOrEmpty()) return

        val reference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toUserReference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val latestMessageReference =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")

        val toUserLatestMessageReference =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")

        val chatMessage = ChatMessage(
            reference.key!!,
            text,
            fromId,
            toId!!,
            System.currentTimeMillis() / 1000
        )

        reference.setValue(chatMessage).addOnSuccessListener {
            editTextMessage.text.clear()
            rvChatLog.scrollToPosition(adapter.itemCount - 1)
        }
        toUserReference.setValue(chatMessage)
        latestMessageReference.setValue(chatMessage)
        toUserLatestMessageReference.setValue(chatMessage)
    }

    private fun configTitleBar(idStatus: Int) {
        if (idStatus == 0) {
            teacherModel = intent.getParcelableExtra(kUserData)!!
        } else {
            studentModel = intent.getParcelableExtra(kUserData)!!
        }

        setSupportActionBar(chatLogTopAppBar)
        chatLogTopAppBar.title =
            if (idStatus == 0) teacherModel.username else "${studentModel.username} - ${studentModel.className}"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
