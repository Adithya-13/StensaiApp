package com.extcode.project.stensaiapps.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.screens.fragments.tasktabs.FinishedFragment
import com.extcode.project.stensaiapps.screens.fragments.tasktabs.UnfinishedFragment

class TaskViewPagerAdapter(private val context: Context, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles = intArrayOf(
        R.string.proses,
        R.string.selesai
    )

    private val fragment = listOf(
        UnfinishedFragment(),
        FinishedFragment()
    )

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(tabTitles[position])
    }

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getCount(): Int = 2

}