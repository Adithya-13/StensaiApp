package com.extcode.project.stensaiapps.screens.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.other.kIdStatus
import com.extcode.project.stensaiapps.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.header_setting.*
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

        websiteButton.setOnClickListener(this)
        signOutButton.setOnClickListener(this)
        closeSetting.setOnClickListener(this)
        sendBug.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.websiteButton -> visitWebsite()
            R.id.signOutButton -> signOut()
            R.id.closeSetting -> finish()
            R.id.sendBug -> sendBug()
        }
    }

    private fun sendBug() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW
            ).apply {
                data =
                    Uri.parse("https://api.whatsapp.com/send?phone=+6281289798423&text=Halo, Saya menemukan sebuah Bug dalam aplikasi Stensai, yaitu...")
            }
        )
    }

    private fun visitWebsite() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW
            ).apply {
                data = Uri.parse("http://stensai-apps.com/")
            }
        )
    }

    private fun signOut() {

        val alertDialogBuilder = AlertDialog.Builder(this, R.style.Dialog)

        alertDialogBuilder.setTitle("Sign Out")
        alertDialogBuilder.setMessage((if (statusId == 0) "Tugas" else "Catatan") + " yang ada di device akan hilang! Apakah ingin melanjutkan?")
            .setCancelable(true)
            .setPositiveButton("Ya") { _, _ ->
                try {
                    GlobalScope.launch(Dispatchers.IO) {
                        val taskViewModel =
                            ViewModelProvider(this@SettingActivity)[TaskViewModel::class.java]
                        taskViewModel.apply {
                            deleteAllUnfinishedTask(this@SettingActivity)
                            deleteAllFinishedTask(this@SettingActivity)
                            deleteAllSchedules(this@SettingActivity)
                        }
                    }
                    getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).apply {
                        edit {
                            clear()
                        }
                    }
                    startActivity(Intent(this, SignInActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    })
                    finishAffinity()
                } catch (e: Exception) {
                    Toast.makeText(this, "Gagal untuk Sign out", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
            .create()
            .show()
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