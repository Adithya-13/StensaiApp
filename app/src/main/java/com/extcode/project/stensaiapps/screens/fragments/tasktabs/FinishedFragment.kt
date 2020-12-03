package com.extcode.project.stensaiapps.screens.fragments.tasktabs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.adapter.FinishedAdapter
import com.extcode.project.stensaiapps.model.db.FinishedData
import com.extcode.project.stensaiapps.other.*
import com.extcode.project.stensaiapps.screens.activity.AddTaskActivity
import com.extcode.project.stensaiapps.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_finished.*

class FinishedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finished, container, false)
    }

    private lateinit var finishedAdapter: FinishedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskViewModel =
            ViewModelProvider(this)[TaskViewModel::class.java]

        configFinishedRecyclerView()
        queryAll(taskViewModel)
    }

    private fun queryAll(taskViewModel: TaskViewModel) {
        showShimmer(shimmer_view_container, true)
        showNotFound(notFound, false)

        taskViewModel.queryAllFinishedTask(context as Context)
            .observe(viewLifecycleOwner, Observer {
                if (it.isNullOrEmpty()) {
                    showShimmer(shimmer_view_container, false)
                    showNotFound(notFound, true)
                    return@Observer
                }

                showShimmer(shimmer_view_container, false)
                showNotFound(notFound, false)
                finishedAdapter.finishedList = it as ArrayList<FinishedData>
                finishedAdapter.notifyDataSetChanged()
            })
    }

    private fun configFinishedRecyclerView() {
        finishedAdapter = FinishedAdapter()
        finishedAdapter.notifyDataSetChanged()

        rvFinished.layoutManager = LinearLayoutManager(context as Context)
        rvFinished.adapter = finishedAdapter

        finishedAdapter.setOnFinishedItemClickCallback(object : OnFinishedItemClickCallback {
            override fun sendFinishedData(finishedData: FinishedData) {
                startActivity(Intent(context as Context, AddTaskActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra(kFinishedData, finishedData)
                    putExtra(kIsFinished, true)
                })
            }
        })
    }

}