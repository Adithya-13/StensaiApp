package com.extcode.project.stensaiapps.screens.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.adapter.chat.LatestMessageRow
import com.extcode.project.stensaiapps.model.ChatMessage
import com.extcode.project.stensaiapps.other.*
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*

class LatestMessagesActivity : AppCompatActivity(), View.OnClickListener {

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val latestMessageMap = HashMap<String, ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        setSupportActionBar(chatTopAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        showProgressBar(progressBar, true)
        showNotFound(notFound, false)

        fabNewMessage.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()

        listenForLatestMessages()
        configLatestMessageRecyclerView()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabNewMessage -> startActivity(Intent(this, NewMessageActivity::class.java))
        }
    }

    private fun refreshRV() {
        adapter.clear()
        latestMessageMap.values.forEach {
            adapter.add(LatestMessageRow(it, this))
        }
    }

    private fun listenForLatestMessages() {

        val fromId = getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).getInt(
            kUid, 0
        ).toString()

        FirebaseDatabase.getInstance().getReference("/latest-messages")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists() || snapshot.value == null) {
                        showProgressBar(progressBar, false)
                        showNotFound(notFound, true)
                        return
                    }

                    val idStatus =
                        getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).getInt(
                            kIdStatus,
                            0
                        )

                    val ref =
                        FirebaseDatabase.getInstance()
                            .getReference("/latest-messages/$fromId-$idStatus")
                    ref.addChildEventListener(object : ChildEventListener {
                        override fun onChildAdded(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                            if (!snapshot.exists()) {
                                showProgressBar(progressBar, false)
                                showNotFound(notFound, true)
                                return
                            }
                            showProgressBar(progressBar, false)
                            showNotFound(notFound, false)
                            val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                            latestMessageMap[snapshot.key!!] = chatMessage
                            refreshRV()
                        }

                        override fun onChildChanged(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                            val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                            latestMessageMap[snapshot.key!!] = chatMessage
                            refreshRV()
                        }

                        override fun onChildRemoved(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {
                                showProgressBar(progressBar, false)
                                showNotFound(notFound, true)
                                return
                            }
                        }

                        override fun onChildMoved(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {

                        }

                        override fun onCancelled(error: DatabaseError) {
                            showProgressBar(progressBar, false)
                            showNotFound(notFound, true)
                            return
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun configLatestMessageRecyclerView() {

        rvLatestMessages.layoutManager = LinearLayoutManager(this)
        rvLatestMessages.adapter = adapter

        val idStatus = getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).getInt(
            kIdStatus,
            0
        )

        adapter.setOnItemClickListener { item, _ ->
            val student = (item as LatestMessageRow).studentData
            val teacher = item.teacherData

            try {
                if ((idStatus == 0 && teacher == null) || (idStatus == 1 && student == null)) {
                    Toast.makeText(this, "User telah dihapus", Toast.LENGTH_SHORT).show()
                    return@setOnItemClickListener
                } else {
                    startActivity(Intent(this, ChatLogActivity::class.java).apply {
                        if (idStatus == 0) {
                            putExtra(kUserData, teacher)
                        } else {
                            putExtra(kUserData, student)
                        }
                    })
                }
            } catch (e: Exception) {
                Toast.makeText(this, "User telah dihapus", Toast.LENGTH_SHORT).show()
            }

        }

        rvLatestMessages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) fabNewMessage.hide() else fabNewMessage.show()
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}