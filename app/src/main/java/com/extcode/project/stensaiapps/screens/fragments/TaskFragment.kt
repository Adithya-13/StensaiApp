package com.extcode.project.stensaiapps.screens.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.adapter.TaskViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_task.*

class TaskFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configViewPager()
    }

    override fun onResume() {
        super.onResume()
        configViewPager()
    }

    private fun configViewPager() {
        val taskViewPagerAdapter = TaskViewPagerAdapter(context as Context, childFragmentManager)
        taskViewPager.adapter = taskViewPagerAdapter
        taskTabs.setupWithViewPager(taskViewPager)
    }
}