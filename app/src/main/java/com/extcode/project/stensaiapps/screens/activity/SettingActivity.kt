package com.extcode.project.stensaiapps.screens.activity

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.other.*
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.student_setting_field.*
import kotlinx.android.synthetic.main.student_setting_field.settingName
import kotlinx.android.synthetic.main.teacher_setting_field.*

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val statusId = getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).getInt(
            kIdStatus,
            0
        )

        getPreviousData(statusId)

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

            settingName.editText?.setText(name)
            settingNIP.editText?.setText(nip.toString())
        }
    }

    private fun setAutoCompleteAdapter(items: List<String>, textInputLayout: TextInputLayout) {
        val adapter = ArrayAdapter(this, R.layout.dropdown_list_item, items)
        (textInputLayout.editText as AutoCompleteTextView).setAdapter(adapter)
    }
}