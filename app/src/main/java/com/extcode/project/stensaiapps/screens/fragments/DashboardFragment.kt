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
import com.extcode.project.stensaiapps.adapter.DashboardMagazineAdapter
import com.extcode.project.stensaiapps.adapter.DashboardSchedulesAdapter
import com.extcode.project.stensaiapps.adapter.DashboardTaskAdapter
import com.extcode.project.stensaiapps.model.db.UnfinishedData
import com.extcode.project.stensaiapps.other.dummyMagazinesList
import com.extcode.project.stensaiapps.other.dummySchedulesList
import com.extcode.project.stensaiapps.screens.activity.LatestMessagesActivity
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
    private lateinit var dashboardMagazineAdapter: DashboardMagazineAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskViewModel =
            ViewModelProvider(this).get(TaskViewModel::class.java)

        configSchedulesRecyclerView()
        configTasksRecyclerView()
        configMagazinesRecyclerView()
        configExtendedFAB()
        queryAll(taskViewModel)

    }

    private fun configExtendedFAB() {
        scrollContainer.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) fabChat.hide() else fabChat.show()
        })

        fabChat.setOnClickListener {
            startActivity(Intent(context, LatestMessagesActivity::class.java))
        }
    }

    private fun queryAll(taskViewModel: TaskViewModel) {
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

    private fun configSchedulesRecyclerView() {
        dashboardSchedulesAdapter = DashboardSchedulesAdapter()
        dashboardSchedulesAdapter.schedulesList = dummySchedulesList
        dashboardSchedulesAdapter.notifyDataSetChanged()

        rvSchedule.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvSchedule.adapter = dashboardSchedulesAdapter
    }

    private fun configTasksRecyclerView() {
        dashboardTaskAdapter = DashboardTaskAdapter()
        dashboardTaskAdapter.notifyDataSetChanged()

        rvTask.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvTask.adapter = dashboardTaskAdapter
    }

    private fun configMagazinesRecyclerView() {
        dashboardMagazineAdapter = DashboardMagazineAdapter()
        dashboardMagazineAdapter.notifyDataSetChanged()

        rvMagazine.layoutManager =
            LinearLayoutManager(context)
        rvMagazine.adapter = dashboardMagazineAdapter
    }

    private fun showProgressBar(view: View, isShow: Boolean) {
        view.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
    }

    private fun showNotFound(view: View, isShow: Boolean) {
        view.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
    }

}