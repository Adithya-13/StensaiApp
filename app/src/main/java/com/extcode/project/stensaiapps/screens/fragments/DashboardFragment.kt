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
import com.extcode.project.stensaiapps.adapter.DashboardTaskAdapter
import com.extcode.project.stensaiapps.adapter.DashboardTaskFinishedAdapter
import com.extcode.project.stensaiapps.model.api.EventItem
import com.extcode.project.stensaiapps.model.db.FinishedData
import com.extcode.project.stensaiapps.model.db.UnfinishedData
import com.extcode.project.stensaiapps.other.kIdStatus
import com.extcode.project.stensaiapps.screens.activity.LatestMessagesActivity
import com.extcode.project.stensaiapps.screens.activity.SignInActivity
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

    private lateinit var dashboardTaskAdapter: DashboardTaskAdapter
    private lateinit var dashboardTaskFinishedAdapter: DashboardTaskFinishedAdapter
    private lateinit var dashboardEventAdapter: DashboardEventAdapter
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var taskViewModel: TaskViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idStatus =
            context?.getSharedPreferences(SignInActivity::class.simpleName, Context.MODE_PRIVATE)
                ?.getInt(
                    kIdStatus, 0
                )

        taskTitle.text =
            if (idStatus == 0) context?.getString(R.string.tugas) else context?.getString(R.string.catatan)

        dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        getEvent()
        configTasksRecyclerView()
        configFinishedTasksRecyclerView()
        configEventRecyclerView()
        configExtendedFAB()
        queryAll()
        queryAllFinished()
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


    private fun queryAllFinished() {
        showProgressBar(progressBarFinished, true)
        showNotFound(notFoundFinished, false)

        taskViewModel.queryAllFinishedTask(context as Context)
            .observe(viewLifecycleOwner, Observer {
                if (it.isNullOrEmpty()) {
                    showProgressBar(progressBarFinished, false)
                    showNotFound(notFoundFinished, true)
                    return@Observer
                }

                showProgressBar(progressBarFinished, false)
                showNotFound(notFoundFinished, false)
                dashboardTaskFinishedAdapter.tasksList = it as ArrayList<FinishedData>
                dashboardTaskFinishedAdapter.notifyDataSetChanged()
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

    private fun configFinishedTasksRecyclerView() {
        dashboardTaskFinishedAdapter = DashboardTaskFinishedAdapter()
        dashboardTaskFinishedAdapter.notifyDataSetChanged()

        with(rvTaskFinished) {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = dashboardTaskFinishedAdapter
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