package com.extcode.project.stensaiapps.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.other.kIdStatus
import com.extcode.project.stensaiapps.screens.activity.SignInActivity
import com.extcode.project.stensaiapps.screens.fragments.tasktabs.FinishedFragment
import com.extcode.project.stensaiapps.screens.fragments.tasktabs.ScheduleFragment
import com.extcode.project.stensaiapps.screens.fragments.tasktabs.UnfinishedFragment

class TaskViewPagerAdapter(
    private val context: Context,
    fragmentManager: FragmentManager
) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val idStatus: Int =
        context.getSharedPreferences(SignInActivity::class.simpleName, Context.MODE_PRIVATE).getInt(
            kIdStatus, 0
        )

    @StringRes
    private val tabTitles = intArrayOf(
        R.string.proses,
        R.string.selesai
    )

    @StringRes
    private val tabTitlesTeacher = intArrayOf(
        R.string.jadwal,
        R.string.catatan
    )

    private val fragment = listOf(
        UnfinishedFragment(),
        FinishedFragment()
    )

    private val fragmentTeacher = listOf(
        ScheduleFragment(),
        UnfinishedFragment()
    )

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(if (idStatus == 0) tabTitles[position] else tabTitlesTeacher[position])
    }

    override fun getItem(position: Int): Fragment {
        return if (idStatus == 0) fragment[position] else fragmentTeacher[position]
    }

    override fun getCount(): Int = 2

}