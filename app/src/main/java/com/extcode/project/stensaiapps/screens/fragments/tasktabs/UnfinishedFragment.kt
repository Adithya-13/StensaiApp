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
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.adapter.UnfinishedAdapter
import com.extcode.project.stensaiapps.model.db.UnfinishedData
import com.extcode.project.stensaiapps.other.*
import com.extcode.project.stensaiapps.screens.activity.AddTaskActivity
import com.extcode.project.stensaiapps.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_unfinished.*

class UnfinishedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unfinished, container, false)
    }

    private lateinit var unfinishedAdapter: UnfinishedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskViewModel =
            ViewModelProvider(this).get(TaskViewModel::class.java)

        configUnfinishedRecyclerView()
        queryAll(taskViewModel)

        fabTask.setOnClickListener {
            startActivity(Intent(context as Context, AddTaskActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
        }
    }

    private fun queryAll(taskViewModel: TaskViewModel) {
        showShimmer(shimmer_view_container, true)
        showNotFound(notFound, false)

        taskViewModel.queryAllUnfinishedTask(context as Context)
            .observe(viewLifecycleOwner, Observer {
                if (it.isNullOrEmpty()) {
                    showShimmer(shimmer_view_container, false)
                    showNotFound(notFound, true)
                    return@Observer
                }

                showShimmer(shimmer_view_container, false)
                showNotFound(notFound, false)
                unfinishedAdapter.unfinishedList = it as ArrayList<UnfinishedData>
                unfinishedAdapter.notifyDataSetChanged()
            })
    }

    private fun configUnfinishedRecyclerView() {
        unfinishedAdapter = UnfinishedAdapter()
        unfinishedAdapter.notifyDataSetChanged()

        rvUnfinished.layoutManager = LinearLayoutManager(context as Context)
        rvUnfinished.adapter = unfinishedAdapter

        unfinishedAdapter.setOnUnfinishedItemClickCallback(object : OnUnfinishedItemClickCallback {
            override fun sendUnfinishedData(unfinishedData: UnfinishedData) {
                startActivity(Intent(context as Context, AddTaskActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra(kUnfinishedData, unfinishedData)
                    putExtra(kIsEdit, true)
                })
            }
        })

        rvUnfinished.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) fabTask.hide() else fabTask.show()
            }
        })
    }
}