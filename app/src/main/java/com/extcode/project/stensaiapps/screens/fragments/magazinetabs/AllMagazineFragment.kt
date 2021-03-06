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
import com.extcode.project.stensaiapps.adapter.MagazineAdapter
import com.extcode.project.stensaiapps.model.api.MessageItem
import com.extcode.project.stensaiapps.other.showNotFound
import com.extcode.project.stensaiapps.other.showShimmer
import com.extcode.project.stensaiapps.viewmodel.MagazineViewModel
import kotlinx.android.synthetic.main.fragment_all_magazine.*

class AllMagazineFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_magazine, container, false)
    }

    private lateinit var magazineViewModel: MagazineViewModel
    private lateinit var magazineAdapter: MagazineAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeMagazine.setOnRefreshListener(this)

        magazineViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[MagazineViewModel::class.java]

        magazineViewModel.setMagazines()
        getMagazine()
        onLoadingSwipeRefresh()
        configMagazinesRecyclerView()

    }

    private fun getMagazine() {

        magazineViewModel.getMagazines().observe(viewLifecycleOwner, Observer {
            showShimmer(shimmer_view_container, true)
            showNotFound(notFound, false)

            val data = it.data
            if (data != null && data.isNotEmpty()) {

                showShimmer(shimmer_view_container, false)
                showNotFound(notFound, false)
                swipeMagazine.isRefreshing = false

                magazineAdapter.magazinesList = data as ArrayList<MessageItem>
                magazineAdapter.notifyDataSetChanged()
            } else {
                showShimmer(shimmer_view_container, false)
                showNotFound(notFound, true)
                swipeMagazine.isRefreshing = false
            }
        })
    }


    private fun configMagazinesRecyclerView() {
        magazineAdapter = MagazineAdapter()
        magazineAdapter.notifyDataSetChanged()

        rvAllMagazineFragment.layoutManager =
            LinearLayoutManager(context).apply {
                reverseLayout = true
                stackFromEnd = true
            }
        rvAllMagazineFragment.adapter = magazineAdapter

    }

    override fun onRefresh() {
        magazineViewModel.setMagazines()
        getMagazine()
    }

    private fun onLoadingSwipeRefresh() {
        swipeMagazine.post {
            Runnable {
                magazineViewModel.setMagazines()
                getMagazine()
            }
        }
    }
}