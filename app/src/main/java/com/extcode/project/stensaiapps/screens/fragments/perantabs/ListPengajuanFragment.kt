package com.extcode.project.stensaiapps.screens.fragments.perantabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.adapter.ListPengajuanAdapter
import com.extcode.project.stensaiapps.model.api.PengajuanItem
import com.extcode.project.stensaiapps.other.showNotFound
import com.extcode.project.stensaiapps.other.showShimmer
import com.extcode.project.stensaiapps.viewmodel.PeranViewModel
import kotlinx.android.synthetic.main.fragment_list_pengajuan.*

class ListPengajuanFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_pengajuan, container, false)
    }

    private lateinit var listPengajuanAdapter: ListPengajuanAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipePengajuan.setOnRefreshListener(this)

        getPengajuan()
        onLoadingSwipeRefresh()
        configPengajuanRecyclerView()
    }

    private fun configPengajuanRecyclerView() {
        listPengajuanAdapter = ListPengajuanAdapter()
        listPengajuanAdapter.notifyDataSetChanged()

        rvPengajuan.layoutManager =
            LinearLayoutManager(context).apply {
                reverseLayout = true
                stackFromEnd = true
            }
        rvPengajuan.setHasFixedSize(true)
        rvPengajuan.adapter = listPengajuanAdapter

    }


    private fun getPengajuan() {
        val peranViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[PeranViewModel::class.java]

        peranViewModel.getPengajuan().observe(viewLifecycleOwner, Observer {
            showShimmer(shimmer_view_container, true)
            showNotFound(notFound, false)

            val data = it.data
            if (data != null && data.isNotEmpty()) {

                showShimmer(shimmer_view_container, false)
                showNotFound(notFound, false)
                swipePengajuan.isRefreshing = false

                listPengajuanAdapter.pengajuanList = data as ArrayList<PengajuanItem>
                listPengajuanAdapter.notifyDataSetChanged()
            } else {
                showShimmer(shimmer_view_container, false)
                showNotFound(notFound, true)
                swipePengajuan.isRefreshing = false
            }
        })
    }


    override fun onRefresh() {
        getPengajuan()
    }

    private fun onLoadingSwipeRefresh() {
        swipePengajuan.post {
            Runnable {
                getPengajuan()
            }
        }
    }
}