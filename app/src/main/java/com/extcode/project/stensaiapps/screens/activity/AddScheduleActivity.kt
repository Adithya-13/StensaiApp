package com.extcode.project.stensaiapps.screens.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.db.ScheduleData
import com.extcode.project.stensaiapps.other.day
import com.extcode.project.stensaiapps.other.kIdSchedule
import com.extcode.project.stensaiapps.other.kIsEditSchedule
import com.extcode.project.stensaiapps.other.kScheduleData
import com.extcode.project.stensaiapps.screens.fragments.datetime.TimePickerFragment
import com.extcode.project.stensaiapps.service.AlarmReceiver
import com.extcode.project.stensaiapps.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.header_add_schedule.*
import kotlinx.android.synthetic.main.layout_schedule.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddScheduleActivity : AppCompatActivity(), View.OnClickListener,
    TimePickerFragment.DialogTimeListener {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var scheduleData: ScheduleData
    private lateinit var getScheduleData: ScheduleData
    private lateinit var scheduleSharedPref: SharedPreferences
    private lateinit var alarmReceiver: AlarmReceiver
    private var dayCode: Int = 0
    private var isDoubleBack = false
    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        scheduleSharedPref =
            getSharedPreferences(AddScheduleActivity::class.simpleName, MODE_PRIVATE)
        isEdit = intent.getBooleanExtra(kIsEditSchedule, false)
        alarmReceiver = AlarmReceiver()

        configSpinnerSchedule()
        configIsEdit()

        saveSchedule.setOnClickListener(this)
        scheduleTime.setOnClickListener(this)
        closeSchedule.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.scheduleTime -> TimePickerFragment().show(supportFragmentManager, "timePicker")
            R.id.closeSchedule -> deleteSchedule()
            R.id.saveSchedule -> saveSchedule()
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        scheduleTime.text = dateFormat.format(calendar.time)
    }

    private fun configIsEdit() {
        if (isEdit) {
            closeSchedule.visibility = View.VISIBLE

            getScheduleData = intent.getParcelableExtra(kScheduleData)!!
            insertTitleSchedule.editText?.setText(getScheduleData.title)
            insertDescriptionSchedule.editText?.setText(getScheduleData.description)
            scheduleTime.text = getScheduleData.time
            spinnerDay.setSelection(getScheduleData.dayCode!! + 1)
        } else {
            closeSchedule.visibility = View.GONE
        }
    }

    private fun saveSchedule() {
        val title = insertTitleSchedule.editText?.text.toString()
        val description = insertDescriptionSchedule.editText?.text.toString()
        val time = scheduleTime.text.toString()

        var id = scheduleSharedPref.getInt(kIdSchedule, 3)

        scheduleData = ScheduleData(
            if (isEdit) getScheduleData.id else id, title, description, dayCode, time
        )

        Log.d("logDayCode", scheduleData.dayCode.toString())

        when {
            title.isEmpty() -> toastError("Judul tidak boleh kosong!")
            description.isEmpty() -> toastError("Deskripsi tidak boleh kosong!")
            time == "Waktu" -> toastError("Waktu tidak boleh kosong!")
            spinnerDay.selectedItem as String == "Hari" -> toastError("Hari tidak boleh kosong!")
            else -> {
                scheduleSharedPref.edit { putInt(kIdSchedule, ++id) }
                if (isEdit) alarmReceiver.cancelAlarm(this, getScheduleData.id)

                alarmReceiver.setAlarmSchedule(
                    this,
                    scheduleData.id,
                    getString(
                        R.string.titleSchedule,
                        scheduleData.title
                    ),
                    "Waktunya ${scheduleData.title} jam ${scheduleData.time}",
                    scheduleData.dayCode!!,
                    scheduleData.time!!
                )
                insertSchedule()
                finish()
            }
        }
    }

    private fun insertSchedule() {
        GlobalScope.launch(Dispatchers.IO) {
            taskViewModel =
                ViewModelProvider(this@AddScheduleActivity).get(
                    TaskViewModel::class.java
                )
            taskViewModel.insertSchedule(this@AddScheduleActivity, scheduleData)
        }
    }

    private fun deleteSchedule() {
        val alertDialogBuilder = AlertDialog.Builder(this, R.style.Dialog)

        alertDialogBuilder.setTitle("Menghapus Jadwal")
        alertDialogBuilder.setMessage("Jadwal yang kamu masukan akan hilang!")
            .setCancelable(true)
            .setPositiveButton("Ya") { _, _ ->
                try {

                    GlobalScope.launch(Dispatchers.IO) {
                        taskViewModel =
                            ViewModelProvider(this@AddScheduleActivity).get(TaskViewModel::class.java)
                        taskViewModel.deleteSchedule(
                            this@AddScheduleActivity,
                            getScheduleData.id
                        )
                    }
                    alarmReceiver.cancelAlarm(this, getScheduleData.id)
                    finish()
                } catch (e: Exception) {
                    toastError("Gagal menghapus Jadwal")
                }
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    private fun configSpinnerSchedule() {
        spinnerDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                dayCode = position - 1
                Log.d("dayCode", dayCode.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        setAutoCompleteAdapter(day, spinnerDay)
    }

    private fun setAutoCompleteAdapter(items: List<String>, spinner: Spinner) {
        val adapter = ArrayAdapter(this, R.layout.dropdown_list_item, items)
        spinner.adapter = adapter
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
            "Jadwal yang kamu masukan akan hilang, tekan lagi untuk keluar",
            Toast.LENGTH_LONG
        ).show()

        Handler().postDelayed({ isDoubleBack = false }, 2000)

    }
}