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
import com.extcode.project.stensaiapps.adapter.DashboardSchedulesAdapter
import com.extcode.project.stensaiapps.adapter.DashboardTaskAdapter
import com.extcode.project.stensaiapps.model.api.EventItem
import com.extcode.project.stensaiapps.model.db.UnfinishedData
import com.extcode.project.stensaiapps.other.dummySchedulesList
import com.extcode.project.stensaiapps.screens.activity.LatestMessagesActivity
import com.extcode.project.stensaiapps.viewmodel.DashboardViewModel
import com.extcode.project.stensaiapps.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    private lateinit var dashboardSchedulesAdapter: DashboardSchedulesAdapter
    private lateinit var dashboardTaskAdapter: DashboardTaskAdapter
    private lateinit var dashboardEventAdapter: DashboardEventAdapter
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var taskViewModel: TaskViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        getEvent()
        configSchedulesRecyclerView()
        configTasksRecyclerView()
        configEventRecyclerView()
        configExtendedFAB()
        queryAll()

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

    private fun getEvent() {
        dashboardViewModel.getEvent().observe(viewLifecycleOwner, Observer {
            showProgressBar(progressBarEvent, true)
            showNotFound(notFoundEvent, false)
            val message = it.event
            if (message != null && message.isNotEmpty()) {
                showProgressBar(progressBarEvent, false)
                showNotFound(notFoundEvent, false)
                dashboardEventAdapter.eventList = message as ArrayList<EventItem>
                dashboardEventAdapter.notifyDataSetChanged()
            } else {
                showProgressBar(progressBarEvent, false)
                showNotFound(notFoundEvent, true)
            }
        })
    }

    private fun configSchedulesRecyclerView() {
        dashboardSchedulesAdapter = DashboardSchedulesAdapter()
        dashboardSchedulesAdapter.schedulesList = dummySchedulesList
        dashboardSchedulesAdapter.notifyDataSetChanged()

        with(rvSchedule) {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = dashboardSchedulesAdapter
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