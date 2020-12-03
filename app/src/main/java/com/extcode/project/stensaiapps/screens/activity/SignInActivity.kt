package com.extcode.project.stensaiapps.screens.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.api.StudentData
import com.extcode.project.stensaiapps.model.api.TeacherData
import com.extcode.project.stensaiapps.other.*
import com.extcode.project.stensaiapps.viewmodel.SignInViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.field_sign_in.*

class SignInActivity : AppCompatActivity() {

    private var isStudent = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
        setAutoCompleteAdapter(statuses, signInStatus)

        (signInStatus.editText as AutoCompleteTextView).onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> isStudent = true
                    1 -> isStudent = false
                    else -> {
                        toastCustom("error")
                    }
                }
            }

        signInButton.setOnClickListener { signIn() }
    }

    private fun signIn() {
        val email = signInEmail.editText?.text.toString()
        val password = signInPassword.editText?.text.toString()

        when {
            email.isEmpty() -> toastEmpty("Email")
            password.isEmpty() -> toastEmpty("Password")
            !email.isValidEmail() -> toastCustom("Format Email Salah!")
            else -> {
                isLoading(true)
                performSignIn(email, password)
            }
        }
    }

    private fun performSignIn(email: String, password: String) {
        ViewModelProvider(this)[SignInViewModel::class.java].apply {
            if (isStudent) {
                this.setStudentSignIn(email, password).observe(this@SignInActivity, Observer {
                    if (it.success) {
                        saveToDatabase(it.data, null)
                        getSharedPreferences(
                            SignInActivity::class.simpleName,
                            MODE_PRIVATE
                        ).apply {
                            edit {
                                if (it.data != null) {
                                    putInt(kIdStatus, 0)
                                    putInt(kUid, it.data.id!!)
                                    putBoolean(kHasLoggedIn, true)
                                    putString(kUserName, it.data.nama)
                                    putLong(kUserNIS, it.data.nis!!.toLong())
                                    putString(kUserClass, it.data.kelasId)
                                    putString(kUserEmail, it.data.email)
                                    apply()
                                }
                            }
                        }
                        toastCustom("Sign In Successful!")
                        isLoading(false)
                        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                        finish()
                    } else {
                        isLoading(false)
                        toastCustom(it.message!!)
                    }
                })
            } else {
                this.setTeacherSignIn(email, password).observe(this@SignInActivity, Observer {
                    if (it.success) {
                        saveToDatabase(null, it.data)
                        getSharedPreferences(
                            SignInActivity::class.simpleName,
                            MODE_PRIVATE
                        ).apply {
                            edit {
                                if (it.data != null) {
                                    putInt(kIdStatus, 1)
                                    putInt(kUid, it.data.id!!)
                                    putBoolean(kHasLoggedIn, true)
                                    putString(kUserName, it.data.nama)
                                    putLong(kUserNIP, it.data.nip!!.toLong())
                                    putString(kUserEmail, it.data.email)
                                    apply()
                                }
                            }
                        }
                        toastCustom("Sign In Successful!")
                        isLoading(false)
                        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                        finish()
                    } else {
                        isLoading(false)
                        toastCustom(it.message!!)
                    }
                })
            }
        }
    }

    private fun saveToDatabase(studentData: StudentData?, teacherData: TeacherData?) {
        val user = if (isStudent) "students" else "teachers"
        val uid = if (isStudent) studentData?.id else teacherData?.id

        val ref = FirebaseDatabase.getInstance().getReference("/users/$user/$uid")

        ref.setValue(if (isStudent) studentData else teacherData)
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

    private fun setAutoCompleteAdapter(items: List<String>, textInputLayout: TextInputLayout) {
        val adapter = ArrayAdapter(this, R.layout.dropdown_list_item, items)
        (textInputLayout.editText as AutoCompleteTextView).setAdapter(adapter)
    }
}