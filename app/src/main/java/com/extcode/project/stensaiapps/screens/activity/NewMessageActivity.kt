package com.extcode.project.stensaiapps.screens.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.adapter.StudentNewMessageAdapter
import com.extcode.project.stensaiapps.adapter.TeacherNewMessageAdapter
import com.extcode.project.stensaiapps.model.StudentModel
import com.extcode.project.stensaiapps.model.TeacherModel
import com.extcode.project.stensaiapps.other.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {

    private lateinit var teacherNewMessageAdapter: TeacherNewMessageAdapter
    private lateinit var studentNewMessageAdapter: StudentNewMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        setSupportActionBar(newChatTopAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val idStatus =
            getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).getInt(
                kIdStatus,
                0
            )

        fetchUser(idStatus)
    }

    private fun fetchUser(idStatus: Int) {
        showProgressBar(progressBar, true)
        showNotFound(notFound, false)
        val status = if (idStatus == 0) "teachers" else "students"
        val ref = FirebaseDatabase.getInstance().getReference("/users/$status")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    showProgressBar(progressBar, false)
                    showNotFound(notFound, true)
                    return
                }
                if (status == "teachers") {
                    val arrUser = ArrayList<TeacherModel>()
                    snapshot.children.forEach {
                        val user = it.getValue(TeacherModel::class.java)
                        if (user != null) {
                            arrUser.add(user)
                        }
                    }
                    configNewMessageRecyclerView(idStatus, null, arrUser)

                } else {
                    val arrUser = ArrayList<StudentModel>()
                    snapshot.children.forEach {
                        val user = it.getValue(StudentModel::class.java)
                        if (user != null) {
                            arrUser.add(user)
                        }
                    }
                    configNewMessageRecyclerView(idStatus, arrUser, null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showProgressBar(progressBar, false)
                showNotFound(notFound, true)
                return
            }
        })
    }

    private fun configNewMessageRecyclerView(
        idStatus: Int,
        studentArrUser: ArrayList<StudentModel>?,
        teacherArrUser: ArrayList<TeacherModel>?
    ) {
        if (idStatus == 0) {
            teacherNewMessageAdapter = TeacherNewMessageAdapter()
            if (teacherArrUser != null) {
                teacherNewMessageAdapter.arrNewMessage = teacherArrUser
                showProgressBar(progressBar, false)
                showNotFound(notFound, false)
            } else {
                showProgressBar(progressBar, false)
                showNotFound(notFound, true)
            }
            teacherNewMessageAdapter.notifyDataSetChanged()
            rvNewMessage.layoutManager = LinearLayoutManager(this)
            rvNewMessage.adapter = teacherNewMessageAdapter

            teacherNewMessageAdapter.setOnTeacherNewMessageItemClickCallback(object :
                OnTeacherNewMessageItemClickCallback {
                override fun sendTeacherData(teacherModel: TeacherModel) {
                    startActivity(
                        Intent(
                            this@NewMessageActivity,
                            ChatLogActivity::class.java
                        ).apply {
                            putExtra(kUserData, teacherModel)
                        })
                    finish()
                }
            })
        } else {
            studentNewMessageAdapter = StudentNewMessageAdapter()
            if (studentArrUser != null) {
                studentNewMessageAdapter.arrNewMessage = studentArrUser
                showProgressBar(progressBar, false)
                showNotFound(notFound, false)
            } else {
                showProgressBar(progressBar, false)
                showNotFound(notFound, true)
            }
            studentNewMessageAdapter.notifyDataSetChanged()
            rvNewMessage.layoutManager = LinearLayoutManager(this)
            rvNewMessage.adapter = studentNewMessageAdapter

            studentNewMessageAdapter.setOnStudentNewMessageItemClickCallback(object :
                OnStudentNewMessageItemClickCallback {
                override fun sendStudentData(studentModel: StudentModel) {
                    startActivity(
                        Intent(
                            this@NewMessageActivity,
                            ChatLogActivity::class.java
                        ).apply {
                            putExtra(kUserData, studentModel)
                        })
                    finish()
                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}