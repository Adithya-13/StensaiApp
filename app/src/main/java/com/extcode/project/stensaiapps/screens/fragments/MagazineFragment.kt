package com.extcode.project.stensaiapps.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.adapter.DashboardMagazineAdapter
import com.extcode.project.stensaiapps.other.dummyMagazinesList
import kotlinx.android.synthetic.main.fragment_magazine.*

class MagazineFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_magazine, container, false)
    }

    private lateinit var dashboardMagazineAdapter: DashboardMagazineAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configMagazinesRecyclerView()
    }


    private fun configMagazinesRecyclerView() {
        dashboardMagazineAdapter = DashboardMagazineAdapter()
        dashboardMagazineAdapter.magazinesList = dummyMagazinesList
        dashboardMagazineAdapter.notifyDataSetChanged()

        rvMagazineFragment.layoutManager =
            LinearLayoutManager(context)
        rvMagazineFragment.adapter = dashboardMagazineAdapter
    }

}