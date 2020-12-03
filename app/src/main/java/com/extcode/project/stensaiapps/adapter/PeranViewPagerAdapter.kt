package com.extcode.project.stensaiapps.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.other.kIdStatus
import com.extcode.project.stensaiapps.screens.activity.SignInActivity
import com.extcode.project.stensaiapps.screens.fragments.perantabs.ListPengajuanFragment
import com.extcode.project.stensaiapps.screens.fragments.perantabs.ListSaranFragment
import com.extcode.project.stensaiapps.screens.fragments.perantabs.PengajuanFragment
import com.extcode.project.stensaiapps.screens.fragments.perantabs.SaranFragment

class PeranViewPagerAdapter(private val context: Context, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val idStatus: Int =
        context.getSharedPreferences(SignInActivity::class.simpleName, Context.MODE_PRIVATE).getInt(
            kIdStatus, 0
        )

    @StringRes
    private val tabTitles = intArrayOf(
        R.string.pengajuan,
        R.string.saran
    )

    private val fragment = listOf(
        PengajuanFragment(),
        SaranFragment()
    )

    private val fragmentTeacher: List<Fragment> = listOf(
        ListPengajuanFragment(),
        ListSaranFragment()
    )

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(tabTitles[position])
    }

    override fun getItem(position: Int): Fragment {
        return if (idStatus == 0) fragment[position] else fragmentTeacher[position]
    }

    override fun getCount(): Int = 2

}