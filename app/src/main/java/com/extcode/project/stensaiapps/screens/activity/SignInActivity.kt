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
import androidx.core.view.get
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.StudentModel
import com.extcode.project.stensaiapps.model.TeacherModel
import com.extcode.project.stensaiapps.other.kIdStatus
import com.extcode.project.stensaiapps.other.isValidEmail
import com.extcode.project.stensaiapps.other.statuses
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.field_sign_in.*

class SignInActivity : AppCompatActivity(), View.OnClickListener {

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
        signInStatus[0].callOnClick()


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

        toSignUp.setOnClickListener(this)
        signInButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toSignUp -> startActivity(Intent(this, SignUpActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
            R.id.signInButton -> signIn()
        }
    }


    private fun signIn() {
        val email = signInEmail.editText?.text.toString()
        val password = signInPassword.editText?.text.toString()

        when {
            email.isEmpty() -> toastEmpty("Email")
            password.isEmpty() -> toastEmpty("Password")
            !email.isValidEmail() -> toastCustom("Format Email Salah!")
            else -> {
                val status = if (isStudent) "students" else "teachers"
                isLoading(true)
                performSignIn(email, password, status)
            }
        }
    }

    private fun performSignIn(email: String, password: String, status: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                queryFirebaseDatabase(status)
            }.addOnFailureListener {
                isLoading(false)
                toastCustom("Sign In Failure!, $it")
            }
    }

    private fun queryFirebaseDatabase(status: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/users/$status")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if (status == "students") {
                        checkAvailableAccount(it, isStudent, "Siswa")
                    } else {
                        checkAvailableAccount(it, isStudent, "Guru")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                toastCustom(error.toString())
            }
        })
    }

    private fun checkAvailableAccount(it: DataSnapshot, isStudent: Boolean, status: String) {
        val user = it.getValue(StudentModel::class.java)
        val teacher = it.getValue(TeacherModel::class.java)
        if (FirebaseAuth.getInstance().uid == (if (isStudent) user?.uid else teacher?.uid)) {
            isLoading(false)
            toastCustom("Sign In Successful!")
            getSharedPreferences(
                SignInActivity::class.simpleName,
                MODE_PRIVATE
            ).apply {
                edit {
                    putInt(kIdStatus, if (isStudent) 0 else 1)
                    apply()
                }
            }
            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
            finish()
        } else {
            isLoading(false)
            toastCustom("Akun anda bukan $status")
            return
        }
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