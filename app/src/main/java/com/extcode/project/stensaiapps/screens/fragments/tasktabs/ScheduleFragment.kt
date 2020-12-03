package com.extcode.project.stensaiapps.screens.fragments.tasktabs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.adapter.ScheduleAdapter
import com.extcode.project.stensaiapps.model.db.ScheduleData
import com.extcode.project.stensaiapps.other.showNotFound
import com.extcode.project.stensaiapps.screens.activity.AddScheduleActivity
import com.extcode.project.stensaiapps.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.schedule_friday.*
import kotlinx.android.synthetic.main.schedule_monday.*
import kotlinx.android.synthetic.main.schedule_saturday.*
import kotlinx.android.synthetic.main.schedule_sunday.*
import kotlinx.android.synthetic.main.schedule_thursday.*
import kotlinx.android.synthetic.main.schedule_tuesday.*
import kotlinx.android.synthetic.main.schedule_wednesday.*

class ScheduleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        queryAll()
        configFAB()
    }

    private fun queryAll() {
        isLoading(true)
        val taskViewModel =
            ViewModelProvider(this).get(TaskViewModel::class.java)

        taskViewModel.queryAllSchedules(context as Context)
            .observe(viewLifecycleOwner, Observer {
                if (it.isNullOrEmpty()) {
                    isLoading(false)
                    showNotFoundMonday(true)
                    showNotFoundTuesday(true)
                    showNotFoundWednesday(true)
                    showNotFoundThursday(true)
                    showNotFoundFriday(true)
                    showNotFoundSaturday(true)
                    showNotFoundSunday(true)
                    return@Observer
                }
                isLoading(false)
                Log.d("arraySche", it.toString())
                val arrScheduleData = it as ArrayList<ScheduleData>
                configScheduleRecyclerView(0, arrScheduleData)
                configScheduleRecyclerView(1, arrScheduleData)
                configScheduleRecyclerView(2, arrScheduleData)
                configScheduleRecyclerView(3, arrScheduleData)
                configScheduleRecyclerView(4, arrScheduleData)
                configScheduleRecyclerView(5, arrScheduleData)
                configScheduleRecyclerView(6, arrScheduleData)
            })
    }

    private fun configScheduleRecyclerView(dayCode: Int, arrSchedule: ArrayList<ScheduleData>) {
        scheduleAdapter = ScheduleAdapter()
        scheduleAdapter.notifyDataSetChanged()

        when (dayCode) {
            0 -> {
                rvScheduleMonday.adapter = scheduleAdapter
                selectedDay(0, arrSchedule)
            }
            1 -> {
                rvScheduleTuesday.adapter = scheduleAdapter
                selectedDay(1, arrSchedule)
            }
            2 -> {
                rvScheduleWednesday.adapter = scheduleAdapter
                selectedDay(2, arrSchedule)
            }
            3 -> {
                rvScheduleThursday.adapter = scheduleAdapter
                selectedDay(3, arrSchedule)
            }
            4 -> {
                rvScheduleFriday.adapter = scheduleAdapter
                selectedDay(4, arrSchedule)
            }
            5 -> {
                rvScheduleSaturday.adapter = scheduleAdapter
                selectedDay(5, arrSchedule)
            }
            6 -> {
                rvScheduleSunday.adapter = scheduleAdapter
                selectedDay(6, arrSchedule)
            }
        }

    }

    private fun selectedDay(dayCode: Int, arrSchedule: ArrayList<ScheduleData>) {
        val array = ArrayList<ScheduleData>()
        for (scheduleItem in arrSchedule) {
            if (scheduleItem.dayCode == dayCode) {
                array.add(scheduleItem)
                scheduleAdapter.scheduleLists = array
                scheduleAdapter.notifyDataSetChanged()
            }
        }
        if (array.isEmpty()) {
            when (dayCode) {
                0 -> showNotFoundMonday(true)
                1 -> showNotFoundTuesday(true)
                2 -> showNotFoundWednesday(true)
                3 -> showNotFoundThursday(true)
                4 -> showNotFoundFriday(true)
                5 -> showNotFoundSaturday(true)
                6 -> showNotFoundSunday(true)
            }
        } else {
            when (dayCode) {
                0 -> showNotFoundMonday(false)
                1 -> showNotFoundTuesday(false)
                2 -> showNotFoundWednesday(false)
                3 -> showNotFoundThursday(false)
                4 -> showNotFoundFriday(false)
                5 -> showNotFoundSaturday(false)
                6 -> showNotFoundSunday(false)
            }
        }
        when (dayCode) {
            0 -> countScheduleMonday.text = array.size.toString()
            1 -> countScheduleTuesday.text = array.size.toString()
            2 -> countScheduleWednesday.text = array.size.toString()
            3 -> countScheduleThursday.text = array.size.toString()
            4 -> countScheduleFriday.text = array.size.toString()
            5 -> countScheduleSaturday.text = array.size.toString()
            6 -> countScheduleSunday.text = array.size.toString()

        }
    }

    private fun configFAB() {
        scrollScheduleContainer.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) fabSchedule.hide() else fabSchedule.show()
        })

        fabSchedule.setOnClickListener {
            startActivity(Intent(context, AddScheduleActivity::class.java))
        }
    }

    private fun showNotFoundMonday(boolean: Boolean) {
        showNotFound(notFoundMonday, boolean)
    }

    private fun showNotFoundTuesday(boolean: Boolean) {
        showNotFound(notFoundTuesday, boolean)
    }

    private fun showNotFoundWednesday(boolean: Boolean) {
        showNotFound(notFoundWednesday, boolean)
    }

    private fun showNotFoundThursday(boolean: Boolean) {
        showNotFound(notFoundThursday, boolean)
    }

    private fun showNotFoundFriday(boolean: Boolean) {
        showNotFound(notFoundFriday, boolean)
    }

    private fun showNotFoundSaturday(boolean: Boolean) {
        showNotFound(notFoundSaturday, boolean)
    }

    private fun showNotFoundSunday(boolean: Boolean) {
        showNotFound(notFoundSunday, boolean)
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