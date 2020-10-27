package com.extcode.project.stensaiapps.screens.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.get
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.StudentModel
import com.extcode.project.stensaiapps.model.TeacherModel
import com.extcode.project.stensaiapps.other.classNameItems
import com.extcode.project.stensaiapps.other.isValidEmail
import com.extcode.project.stensaiapps.other.kIdStatus
import com.extcode.project.stensaiapps.other.statuses
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.student_field_sign_up.*
import kotlinx.android.synthetic.main.teacher_field_sign_up.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private var isStudent = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        setAutoCompleteAdapter(statuses, signUpStatus)
        setAutoCompleteAdapter(classNameItems, signUpClassName)

        signUpStatus[0].callOnClick()

        (signUpStatus.editText as AutoCompleteTextView).onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> showStudentField()
                    1 -> showTeacherField()
                    else -> {
                        toastCustom("error")
                    }
                }
            }

        signUpButton.setOnClickListener(this)
        toSignIn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toSignIn -> startActivity(Intent(this, SignInActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
            R.id.signUpButton -> signUp()
        }
    }

    private fun signUp() {
        val studentEmail = signUpEmail.editText?.text.toString().trim()
        val studentName = signUpName.editText?.text.toString().trim()
        val studentNIS = signUpNIS.editText?.text.toString().trim()
        val studentClassName = signUpClassName.editText?.text.toString().trim()
        val studentPassword = signUpPassword.editText?.text.toString()

        val teacherEmail = signUpEmailTeacher.editText?.text.toString().trim()
        val teacherName = signUpNameTeacher.editText?.text.toString().trim()
        val teacherNIP = signUpNIP.editText?.text.toString()
        val teacherPassword = signUpPasswordTeacher.editText?.text.toString()

        if (isStudent) {
            when {
                studentEmail.isEmpty() -> {
                    toastEmpty("Email Siswa")
                    return
                }
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
                studentPassword.isEmpty() -> {
                    toastEmpty("Password Siswa")
                    return
                }
                !studentEmail.isValidEmail() -> {
                    toastCustom("Format Email salah!")
                    return
                }
                else -> {
                    isLoading(true)
                    performSignUp(
                        studentEmail,
                        studentPassword,
                        studentName,
                        studentNIS.toLong(),
                        studentClassName,
                        0
                    )
                }
            }
        } else {
            when {
                teacherEmail.isEmpty() -> {
                    toastEmpty("Email Guru")
                    return
                }
                teacherName.isEmpty() -> {
                    toastEmpty("Nama Guru")
                    return
                }
                teacherNIP.isEmpty() -> {
                    toastEmpty("NIP Guru")
                    return
                }
                teacherPassword.isEmpty() -> {
                    toastEmpty("Password Guru")
                    return
                }
                !teacherEmail.isValidEmail() -> {
                    toastCustom("Format Email salah!")
                    return
                }
                else -> {
                    isLoading(true)
                    performSignUp(
                        teacherEmail,
                        teacherPassword,
                        teacherName,
                        teacherNIP.toLong(),
                        studentClassName,
                        1
                    )
                }
            }
        }
    }

    private fun performSignUp(
        email: String,
        password: String,
        username: String,
        nisOrNip: Long,
        className: String,
        status: Int
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                toastCustom("Create Account Successful")
                saveUserToRealtimeDatabase(username, nisOrNip, className, status)
            }.addOnFailureListener {
                isLoading(false)
                toastCustom("Create Account Failure, $it")
                Log.d("signUpActivity", it.toString())
            }
    }

    private fun showStudentField() {
        signUpStudentField.visibility = View.VISIBLE
        signUpTeacherField.visibility = View.GONE
        isStudent = true
    }

    private fun showTeacherField() {
        signUpStudentField.visibility = View.GONE
        signUpTeacherField.visibility = View.VISIBLE
        isStudent = false
    }

    private fun saveUserToRealtimeDatabase(
        username: String,
        nisOrNip: Long,
        className: String,
        status: Int
    ) {
        val user = if (isStudent) "students" else "teachers"
        val uid = FirebaseAuth.getInstance().uid

        val ref = FirebaseDatabase.getInstance().getReference("/users/$user/$uid")

        ref.setValue(
            if (isStudent) StudentModel(
                uid,
                username,
                nisOrNip,
                className,
                status
            ) else TeacherModel(
                uid,
                username,
                nisOrNip,
                status
            )
        ).addOnSuccessListener {
            isLoading(false)
            getSharedPreferences(
                SignInActivity::class.simpleName,
                MODE_PRIVATE
            ).apply {
                edit {
                    putInt(kIdStatus, if (isStudent) 0 else 1)
                    apply()
                }
            }
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            finish()
        }.addOnFailureListener {
            isLoading(false)
            toastCustom("gagal menyimpan informasi User, $it")
        }
    }

    private fun setAutoCompleteAdapter(items: List<String>, textInputLayout: TextInputLayout) {
        val adapter = ArrayAdapter(this, R.layout.dropdown_list_item, items)
        (textInputLayout.editText as AutoCompleteTextView).setAdapter(adapter)
    }

    private fun toastCustom(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun toastEmpty(view: String) {
        Toast.makeText(this, "$view harus di-isi!", Toast.LENGTH_SHORT).show()
    }

    private fun isLoading(bool: Boolean) {
        if (bool) {
            progressBar.visibility = View.VISIBLE
            loadingBackground.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            loadingBackground.visibility = View.GONE
        }
    }
}