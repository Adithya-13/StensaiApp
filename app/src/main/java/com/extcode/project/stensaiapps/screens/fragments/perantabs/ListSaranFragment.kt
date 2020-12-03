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
import com.extcode.project.stensaiapps.adapter.ListSaranAdapter
import com.extcode.project.stensaiapps.model.api.AllStudentItem
import com.extcode.project.stensaiapps.model.api.SaranItem
import com.extcode.project.stensaiapps.other.showNotFound
import com.extcode.project.stensaiapps.other.showShimmer
import com.extcode.project.stensaiapps.viewmodel.PeranViewModel
import kotlinx.android.synthetic.main.fragment_list_saran.*

class ListSaranFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_saran, container, false)
    }

    private lateinit var peranViewModel: PeranViewModel
    private lateinit var listSaranAdapter: ListSaranAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        peranViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[PeranViewModel::class.java]


        swipeSaran.setOnRefreshListener(this)

        getSaran()
        onLoadingSwipeRefresh()
        configPengajuanRecyclerView()
    }

    private fun configPengajuanRecyclerView() {
        listSaranAdapter = ListSaranAdapter()
        listSaranAdapter.notifyDataSetChanged()

        rvSaran.layoutManager =
            LinearLayoutManager(context).apply {
                reverseLayout = true
                stackFromEnd = true
            }
        rvSaran.setHasFixedSize(true)
        rvSaran.adapter = listSaranAdapter

    }

    private fun getSaran() {

        peranViewModel.getSaran().observe(viewLifecycleOwner, Observer {
            showShimmer(shimmer_view_container, true)
            showNotFound(notFound, false)

            val data = it.data
            if (data != null && data.isNotEmpty()) {
                getSiswaId(data as ArrayList<SaranItem>)
            } else {
                showShimmer(shimmer_view_container, false)
                showNotFound(notFound, true)
                swipeSaran.isRefreshing = false
            }
        })
    }

    private fun getSiswaId(saranItem: ArrayList<SaranItem>) {
        peranViewModel.setSiswa()
        peranViewModel.getSiswa().observe(viewLifecycleOwner, Observer {

            val data = it.data
            if (data != null && data.isNotEmpty()) {

                showShimmer(shimmer_view_container, false)
                showNotFound(notFound, false)
                swipeSaran.isRefreshing = false

                listSaranAdapter.siswaIdList = data as ArrayList<AllStudentItem>
                listSaranAdapter.saranList = saranItem
                listSaranAdapter.notifyDataSetChanged()
            } else {
                showShimmer(shimmer_view_container, false)
                showNotFound(notFound, true)
                swipeSaran.isRefreshing = false
            }
        })
    }

    override fun onRefresh() {
        getSaran()
    }

    private fun onLoadingSwipeRefresh() {
        swipeSaran.post {
            Runnable {
                getSaran()
            }
        }
    }

}