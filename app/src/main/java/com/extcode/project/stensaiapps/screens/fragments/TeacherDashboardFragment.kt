package com.extcode.project.stensaiapps.screens.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.adapter.DashboardEventAdapter
import com.extcode.project.stensaiapps.adapter.DashboardScheduleAdapter
import com.extcode.project.stensaiapps.adapter.DashboardTaskAdapter
import com.extcode.project.stensaiapps.model.api.EventItem
import com.extcode.project.stensaiapps.model.db.ScheduleData
import com.extcode.project.stensaiapps.model.db.UnfinishedData
import com.extcode.project.stensaiapps.screens.activity.LatestMessagesActivity
import com.extcode.project.stensaiapps.viewmodel.DashboardViewModel
import com.extcode.project.stensaiapps.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_teacher_dashboard.*
import java.util.*
import kotlin.collections.ArrayList

class TeacherDashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_dashboard, container, false)
    }

    private lateinit var dashboardScheduleAdapter: DashboardScheduleAdapter
    private lateinit var dashboardTaskAdapter: DashboardTaskAdapter
    private lateinit var dashboardEventAdapter: DashboardEventAdapter
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var taskViewModel: TaskViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        val calendarDay = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
        val dayCode = if (calendarDay <= 1) 6 else calendarDay - 2

        val day = when (dayCode) {
            0 -> "Senin"
            1 -> "Selasa"
            2 -> "Rabu"
            3 -> "Kamis"
            4 -> "Jumat"
            5 -> "Sabtu"
            6 -> "Minggu"
            else -> ""
        }

        scheduleTitle.text = getString(R.string.jadwal_hari_s, day)

        getEvent()
        configTasksRecyclerView()
        configEventRecyclerView()
        configExtendedFAB()
        queryAll()
        queryAllSchedules(dayCode)

    }

    private fun configExtendedFAB() {
        scrollContainer.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) fabChat.hide() else fabChat.show()
        })

        fabChat.setOnClickListener {
            startActivity(Intent(context, LatestMessagesActivity::class.java))
        }
    }

    private fun queryAll() {
        showProgressBar(progressBar, true)
        showNotFound(notFound, false)

        taskViewModel.queryAllUnfinishedTask(context as Context)
            .observe(viewLifecycleOwner, Observer {
                if (it.isNullOrEmpty()) {
                    showProgressBar(progressBar, false)
                    showNotFound(notFound, true)
                    return@Observer
                }

                showProgressBar(progressBar, false)
                showNotFound(notFound, false)
                dashboardTaskAdapter.tasksList = it as ArrayList<UnfinishedData>
                countTask.text = it.size.toString()
                dashboardTaskAdapter.notifyDataSetChanged()
            })
    }

    private fun queryAllSchedules(dayCode: Int) {
        showProgressBar(progressBarSchedule, true)
        showNotFound(notFoundSchedule, false)

        taskViewModel.queryAllSchedules(context as Context)
            .observe(viewLifecycleOwner, Observer {
                if (it.isNullOrEmpty()) {
                    showProgressBar(progressBarSchedule, false)
                    showNotFound(notFoundSchedule, true)
                    return@Observer
                }

                val arrScheduleData = it as ArrayList<ScheduleData>
                configSchedulesRecyclerView(arrScheduleData, dayCode)
            })
    }

    private fun getEvent() {
        dashboardViewModel.setEvent()
        dashboardViewModel.getEvent().observe(viewLifecycleOwner, Observer {
            showProgressBar(progressBarEvent, true)
            showNotFound(notFoundEvent, false)
            val data = it.data
            if (data != null && data.isNotEmpty()) {
                showProgressBar(progressBarEvent, false)
                showNotFound(notFoundEvent, false)
                dashboardEventAdapter.eventList = data as ArrayList<EventItem>
                dashboardEventAdapter.notifyDataSetChanged()
            } else {
                showProgressBar(progressBarEvent, false)
                showNotFound(notFoundEvent, true)
            }
        })
    }

    private fun configSchedulesRecyclerView(
        arrScheduleData: ArrayList<ScheduleData>,
        dayCode: Int
    ) {
        dashboardScheduleAdapter = DashboardScheduleAdapter()
        dashboardScheduleAdapter.notifyDataSetChanged()

        val array = ArrayList<ScheduleData>()
        for (scheduleItem in arrScheduleData) {
            if (scheduleItem.dayCode == dayCode) {
                array.add(scheduleItem)
                dashboardScheduleAdapter.scheduleList = array
                dashboardScheduleAdapter.notifyDataSetChanged()
            }
        }
        if (array.isEmpty()) {
            showProgressBar(progressBarSchedule, false)
            showNotFound(notFoundSchedule, true)
        } else {
            showProgressBar(progressBarSchedule, false)
            showNotFound(notFoundSchedule, false)
        }
        with(rvSchedule) {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = dashboardScheduleAdapter
        }
    }

    private fun configTasksRecyclerView() {
        dashboardTaskAdapter = DashboardTaskAdapter()
        dashboardTaskAdapter.notifyDataSetChanged()

        with(rvTask) {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = dashboardTaskAdapter
        }
    }

    private fun configEventRecyclerView() {
        dashboardEventAdapter = DashboardEventAdapter()
        dashboardEventAdapter.notifyDataSetChanged()

        with(rvEvent) {
            layoutManager =
                LinearLayoutManager(context).apply {
                    reverseLayout = true
                    stackFromEnd = true
                }
            setHasFixedSize(true)
            adapter = dashboardEventAdapter
        }
    }

    private fun showProgressBar(view: View, isShow: Boolean) {
        view.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
    }

    private fun showNotFound(view: View, isShow: Boolean) {
        view.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
    }

}