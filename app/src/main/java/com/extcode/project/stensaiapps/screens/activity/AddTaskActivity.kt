package com.extcode.project.stensaiapps.screens.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.db.FinishedData
import com.extcode.project.stensaiapps.model.db.UnfinishedData
import com.extcode.project.stensaiapps.other.*
import com.extcode.project.stensaiapps.service.AlarmReceiver
import com.extcode.project.stensaiapps.screens.fragments.datetime.DatePickerFragment
import com.extcode.project.stensaiapps.screens.fragments.datetime.TimePickerFragment
import com.extcode.project.stensaiapps.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.deadline_layout_button.*
import kotlinx.android.synthetic.main.header_add_task.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var unfinishedData: UnfinishedData
    private lateinit var finishedData: FinishedData
    private lateinit var getUnfinishedData: UnfinishedData
    private lateinit var getFinishedData: FinishedData
    private lateinit var taskSharedPref: SharedPreferences
    private lateinit var alarmReceiver: AlarmReceiver
    private var isDoubleBack = false
    private var isEdit = false
    private var isFinished = false
    private var switchOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        taskSharedPref = getSharedPreferences(AddTaskActivity::class.simpleName, MODE_PRIVATE)
        isEdit = intent.getBooleanExtra(kIsEdit, false)
        isFinished = intent.getBooleanExtra(kIsFinished, false)
        alarmReceiver = AlarmReceiver()

        checkSwitch()
        configIsEdit()

        saveTask.setOnClickListener(this)
        dlDateTask.setOnClickListener(this)
        dlTimeTask.setOnClickListener(this)
        closeTask.setOnClickListener(this)
        doneTask.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dlDateTask -> DatePickerFragment().show(supportFragmentManager, "datePicker")
            R.id.dlTimeTask -> TimePickerFragment().show(supportFragmentManager, "timePicker")
            R.id.closeTask -> deleteTask()
            R.id.doneTask -> doneTask()
            R.id.saveTask -> saveTask()
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dlDateTask.text = dateFormat.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        dlTimeTask.text = dateFormat.format(calendar.time)
    }

    private fun checkSwitch() {
        dlSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dlDateTask.visibility = View.VISIBLE
                dlTimeTask.visibility = View.VISIBLE
                switchOn = true
            } else {
                dlDateTask.visibility = View.GONE
                dlTimeTask.visibility = View.GONE
                dlDateTask.text = getString(R.string.date)
                dlTimeTask.text = getString(R.string.time)
                switchOn = false
            }
        }
    }

    private fun configIsEdit() {
        if (isEdit) {

            closeTask.visibility = View.VISIBLE
            doneTask.visibility = View.VISIBLE

            getUnfinishedData = intent?.getParcelableExtra(kUnfinishedData)!!
            insertTitleTask.editText?.setText(getUnfinishedData.title)
            insertDescriptionTask.editText?.setText(getUnfinishedData.description)

            if (!getUnfinishedData.date.equals("null") && !getUnfinishedData.time.equals("null")) {
                dlSwitch.isChecked = true
                dlDateTask.text = getUnfinishedData.date
                dlTimeTask.text = getUnfinishedData.time
            } else dlSwitch.isChecked = false

        } else if (isFinished) {

            closeTask.visibility = View.VISIBLE
            doneTask.visibility = View.GONE

            getFinishedData = intent?.getParcelableExtra(kFinishedData)!!
            insertTitleTask.editText?.setText(getFinishedData.title)
            insertDescriptionTask.editText?.setText(getFinishedData.description)

            if (!getFinishedData.date.equals("null") && !getFinishedData.time.equals("null")) {
                dlSwitch.isChecked = true
                dlDateTask.text = getFinishedData.date
                dlTimeTask.text = getFinishedData.time
            } else dlSwitch.isChecked = false

        } else {
            closeTask.visibility = View.GONE
            doneTask.visibility = View.GONE
        }
    }

    private fun saveTask() {

        val title = insertTitleTask.editText?.text.toString()
        val description = insertDescriptionTask.editText?.text.toString()
        val date = dlDateTask.text.toString()
        val time = dlTimeTask.text.toString()

        var id = taskSharedPref.getInt(kId, 3)

        unfinishedData =
            UnfinishedData(
                if (isEdit) getUnfinishedData.id else id, title, description,
                if (switchOn) date else "null",
                if (switchOn) time else "null"
            )

        if (isFinished){
            getFinishedData = intent?.getParcelableExtra(kFinishedData)!!
            finishedData = FinishedData(
                if (isEdit) getFinishedData.id else id,
                title,
                description,
                if (switchOn) date else "null",
                if (switchOn) time else "null"
            )
        }

        if (switchOn) {
            when {
                title.isEmpty() -> toastError("Judul tidak boleh kosong!")
                description.isEmpty() -> toastError("Deskripsi tidak boleh kosong!")
                date == "Date" -> toastError("Tanggal tidak boleh kosong!")
                time == "Time" -> toastError("Waktu tidak boleh kosong!")
                else -> {
                    try {
                        if (isFinished) {
                            insertFinishedTask()
                        } else {
                            taskSharedPref.edit { putInt("id", ++id) }
                            if (isEdit) alarmReceiver.cancelAlarm(this, unfinishedData.id)

                            alarmReceiver.setAlarm(
                                this,
                                unfinishedData.id,
                                getString(R.string.titleDeadlineTask),
                                "${unfinishedData.title} Akan berakhir pada ${unfinishedData.time}, ${unfinishedData.date}",
                                unfinishedData.date!!,
                                unfinishedData.time!!
                            )
                            insertUnfinishedTask()
                        }
                        finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        toastError("Gagal menyimpan Tugas")
                    }
                }
            }
        } else {

            when {

                title.isEmpty() -> toastError("Judul tidak boleh kosong!")
                description.isEmpty() -> toastError("Deskripsi tidak boleh kosong!")

                else -> {
                    try {
                        if (isFinished) {
                            insertFinishedTask()
                        } else {
                            taskSharedPref.edit { putInt(kId, ++id) }
                            insertUnfinishedTask()
                        }
                        finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        toastError("Gagal menyimpan Tugas")
                    }
                }
            }
        }
    }

    private fun insertUnfinishedTask() {
        GlobalScope.launch(Dispatchers.IO) {
            taskViewModel =
                ViewModelProvider(this@AddTaskActivity).get(
                    TaskViewModel::class.java
                )
            taskViewModel.insertUnfinishedTask(this@AddTaskActivity, unfinishedData)
        }
    }

    private fun insertFinishedTask() {
        GlobalScope.launch(Dispatchers.IO) {
            taskViewModel =
                ViewModelProvider(this@AddTaskActivity).get(
                    TaskViewModel::class.java
                )
            taskViewModel.insertFinishedTask(this@AddTaskActivity, finishedData)
        }
    }

    private fun doneTask() {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                taskViewModel =
                    ViewModelProvider(this@AddTaskActivity).get(TaskViewModel::class.java)
                val id = getUnfinishedData.id
                val title = getUnfinishedData.title
                val description = getUnfinishedData.description
                val date = getUnfinishedData.date
                val time = getUnfinishedData.time

                taskViewModel.deleteUnfinishedTask(this@AddTaskActivity, getUnfinishedData.id)
                finishedData = FinishedData(id, title, description, date, time)
                taskViewModel.insertFinishedTask(this@AddTaskActivity, finishedData)
            }
            finish()
        } catch (e: Exception) {
            toastError("Gagal memindahkan tugas ke list selesai")
        }
    }


    private fun deleteTask() {

        val alertDialogBuilder = AlertDialog.Builder(this, R.style.Dialog)

        alertDialogBuilder.setTitle("Menghapus Tugas")
        alertDialogBuilder.setMessage("Tugas yang kamu masukan akan hilang!")
            .setCancelable(true)
            .setPositiveButton("Ya") { _, _ ->
                try {
                    if (isFinished) {
                        getFinishedData =
                            intent?.getParcelableExtra(kFinishedData)!!
                        GlobalScope.launch(Dispatchers.IO) {
                            taskViewModel =
                                ViewModelProvider(this@AddTaskActivity).get(TaskViewModel::class.java)
                            taskViewModel.deleteFinishedTask(
                                this@AddTaskActivity,
                                getFinishedData.id
                            )
                        }
                    } else {
                        GlobalScope.launch(Dispatchers.IO) {
                            taskViewModel =
                                ViewModelProvider(this@AddTaskActivity).get(TaskViewModel::class.java)
                            taskViewModel.deleteUnfinishedTask(
                                this@AddTaskActivity,
                                getUnfinishedData.id
                            )
                        }
                    }
                    finish()
                } catch (e: Exception) {
                    toastError("Gagal menghapus Tugas")
                }
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    private fun toastError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (isDoubleBack) {
            super.onBackPressed()
            return
        }

        this.isDoubleBack = true
        Toast.makeText(
            this,
            "Tugas yang kamu masukan akan hilang, tekan lagi untuk keluar",
            Toast.LENGTH_LONG
        ).show()

        Handler().postDelayed({ isDoubleBack = false }, 2000)

    }
}