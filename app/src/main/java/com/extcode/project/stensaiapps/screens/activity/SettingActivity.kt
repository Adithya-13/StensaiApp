package com.extcode.project.stensaiapps.screens.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.StudentModel
import com.extcode.project.stensaiapps.model.TeacherModel
import com.extcode.project.stensaiapps.other.*
import com.extcode.project.stensaiapps.viewmodel.TaskViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.header_setting.*
import kotlinx.android.synthetic.main.student_setting_field.*
import kotlinx.android.synthetic.main.teacher_setting_field.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    private var statusId = 0
    private var isDoubleBack = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        statusId = getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).getInt(
            kIdStatus,
            0
        )

        getPreviousData(statusId)

        resetButton.setOnClickListener(this)
        signOutButton.setOnClickListener(this)
        closeSetting.setOnClickListener(this)
        saveSetting.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.resetButton -> sendResetPassword()
            R.id.signOutButton -> signOut()
            R.id.closeSetting -> close()
            R.id.saveSetting -> save()
        }
    }

    private fun sendResetPassword() {
        val email = getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).getString(
            kUserEmail,
            ""
        )
        if (email != null) {
            try {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                toastCustom("Reset password terkirim, harap check email")
            } catch (e: Exception) {
                toastCustom("Reset Password gagal terkirim")
            }
        }
    }

    private fun signOut() {

        val alertDialogBuilder = AlertDialog.Builder(this, R.style.Dialog)

        alertDialogBuilder.setTitle("Sign Out")
        alertDialogBuilder.setMessage("Tugas yang ada di device akan hilang! Apakah ingin melanjutkan?")
            .setCancelable(true)
            .setPositiveButton("Ya") { _, _ ->
                try {
                    GlobalScope.launch(Dispatchers.IO) {
                        val taskViewModel =
                            ViewModelProvider(this@SettingActivity)[TaskViewModel::class.java]
                        taskViewModel.apply {
                            deleteAllUnfinishedTask(this@SettingActivity)
                            deleteAllFinishedTask(this@SettingActivity)
                        }
                    }
                    getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).apply {
                        edit {
                            clear()
                        }
                    }
                    startActivity(Intent(this, SignInActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    })
                    finish()
                } catch (e: Exception) {
                    toastCustom("Gagal untuk Sign out")
                }
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    private fun close() {
        finish()
    }

    private fun save() {
        showProgressBar(progressBar, true)

        val studentName = settingName.editText?.text.toString().trim()
        val studentNIS = settingNIS.editText?.text.toString().trim()
        val studentClassName = settingClassName.editText?.text.toString().trim()

        val teacherName = settingNameTeacher.editText?.text.toString().trim()
        val teacherNIP = settingNIP.editText?.text.toString()


        if (statusId == 0) {
            when {
                studentName.isEmpty() -> {
                    toastEmpty("Nama Siswa")
                    return
                }
                studentNIS.isEmpty() -> {
                    toastEmpty("NIS Siswa")
                    return
                }
                studentClassName.isEmpty() -> {
                    toastEmpty("Kelas Siswa")
                    return
                }
                else -> changeSetting(studentName, studentNIS.toLong(), studentClassName)
            }
        } else {
            when {
                teacherName.isEmpty() -> {
                    toastEmpty("Nama Guru")
                    return
                }
                teacherNIP.isEmpty() -> {
                    toastEmpty("NIP Guru")
                    return
                }
                else -> changeSetting(teacherName, teacherNIP.toLong(), studentClassName)
            }
        }
    }

    private fun changeSetting(name: String, nisOrNip: Long, className: String) {

        val uid = FirebaseAuth.getInstance().uid
        val status = if (statusId == 0) "students" else "teachers"

        val ref = FirebaseDatabase.getInstance().getReference("/users/$status/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (statusId == 0) {
                    val user = snapshot.getValue(StudentModel::class.java)
                    ref.setValue(
                        StudentModel(
                            uid,
                            name,
                            user?.email,
                            nisOrNip,
                            className,
                            0
                        )
                    )
                    getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).apply {
                        edit {
                            if (user != null) {
                                putString(kUserName, name)
                                putLong(kUserNIS, nisOrNip)
                                putString(kUserClass, className)
                                putString(kUserEmail, user.email)
                                apply()
                                commit()
                            }
                        }
                    }
                    showProgressBar(progressBar, false)
                    startActivity(Intent(this@SettingActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    })
                } else {
                    val user = snapshot.getValue(TeacherModel::class.java)
                    ref.setValue(
                        TeacherModel(
                            uid,
                            name,
                            user?.email,
                            nisOrNip,
                            1
                        )
                    )
                    getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).apply {
                        edit {
                            putString(kUserName, name)
                            putLong(kUserNIP, nisOrNip)
                            putString(kUserEmail, user?.email)
                            apply()
                            commit()
                        }
                    }
                    showProgressBar(progressBar, false)
                    startActivity(Intent(this@SettingActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                toastCustom("Gagal menyimpan data, $error")
            }
        })
    }

    private fun getPreviousData(idStatus: Int) {
        if (idStatus == 0) {
            studentSettingContainer.visibility = View.VISIBLE
            teacherSettingContainer.visibility = View.GONE

            val name: String?
            val nis: Long?
            val className: String?
            getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).apply {
                name = this.getString(kUserName, "User")
                nis = this.getLong(kUserNIS, 0)
                className = this.getString(kUserClass, "")
            }

            settingName.editText?.setText(name)
            settingNIS.editText?.setText(nis.toString())
            settingClassName.editText?.setText(className)
            setAutoCompleteAdapter(classNameItems, settingClassName)

        } else {
            studentSettingContainer.visibility = View.GONE
            teacherSettingContainer.visibility = View.VISIBLE

            val name: String?
            val nip: Long?
            getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).apply {
                name = this.getString(kUserName, "User")
                nip = this.getLong(kUserNIP, 0)
            }

            settingNameTeacher.editText?.setText(name)
            settingNIP.editText?.setText(nip.toString())
        }
    }

    private fun toastCustom(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun toastEmpty(view: String) {
        Toast.makeText(this, "$view harus di-isi!", Toast.LENGTH_SHORT).show()
    }

    private fun setAutoCompleteAdapter(items: List<String>, textInputLayout: TextInputLayout) {
        val adapter = ArrayAdapter(this, R.layout.dropdown_list_item, items)
        (textInputLayout.editText as AutoCompleteTextView).setAdapter(adapter)
    }

    override fun onBackPressed() {
        if (isDoubleBack) {
            super.onBackPressed()
            return
        }

        isDoubleBack = true
        Toast.makeText(
            this,
            "Pengaturan yang kamu ubah akan tidak tersimpan",
            Toast.LENGTH_LONG
        ).show()

        Handler().postDelayed({ isDoubleBack = false }, 2000)

    }
}