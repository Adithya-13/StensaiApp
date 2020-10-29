package com.extcode.project.stensaiapps.screens.fragments.magazinetabs

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
import com.extcode.project.stensaiapps.adapter.DashboardMagazineAdapter
import com.extcode.project.stensaiapps.model.api.MessageItem
import com.extcode.project.stensaiapps.other.showNotFound
import com.extcode.project.stensaiapps.other.showShimmer
import com.extcode.project.stensaiapps.viewmodel.MagazineViewModel
import kotlinx.android.synthetic.main.fragment_all_magazine.notFound
import kotlinx.android.synthetic.main.fragment_all_magazine.shimmer_view_container
import kotlinx.android.synthetic.main.fragment_all_magazine.swipeMagazine
import kotlinx.android.synthetic.main.fragment_karya_magazine.*

class KaryaMagazineFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_karya_magazine, container, false)
    }

    private lateinit var dashboardMagazineAdapter: DashboardMagazineAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeMagazine.setColorSchemeColors(context!!.resources.getColor(R.color.colorPrimaryDark))
        swipeMagazine.setOnRefreshListener(this)

        getMagazine()
        onLoadingSwipeRefresh()
        configMagazinesRecyclerView()

    }

    private fun getMagazine() {
        val magazineViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[MagazineViewModel::class.java]

        magazineViewModel.getMagazines().observe(viewLifecycleOwner, Observer {
            showShimmer(shimmer_view_container, true)
            showNotFound(notFound, false)
            swipeMagazine.isRefreshing = true


            val message = it.message as ArrayList<MessageItem>

            if (message.isNotEmpty()) {

                showShimmer(shimmer_view_container, false)
                showNotFound(notFound, false)
                swipeMagazine.isRefreshing = false

                val karyaMessage = ArrayList<MessageItem>()
                for (messageItem in message) {
                    if (messageItem.kategoriId?.toInt() == 1) {
                        karyaMessage.add(messageItem)
                    }
                }
                dashboardMagazineAdapter.magazinesList = karyaMessage
                dashboardMagazineAdapter.notifyDataSetChanged()
            } else {
                showShimmer(shimmer_view_container, false)
                showNotFound(notFound, true)
                swipeMagazine.isRefreshing = false
            }
        })
    }


    private fun configMagazinesRecyclerView() {
        dashboardMagazineAdapter = DashboardMagazineAdapter()
        dashboardMagazineAdapter.notifyDataSetChanged()

        rvKaryaMagazineFragment.layoutManager =
            LinearLayoutManager(context).apply {
                reverseLayout = true
                stackFromEnd = true
            }
        rvKaryaMagazineFragment.adapter = dashboardMagazineAdapter

    }

    override fun onRefresh() {
        getMagazine()
    }

    private fun onLoadingSwipeRefresh() {
        swipeMagazine.post {
            Runnable {
                getMagazine()
            }
        }
    }
}