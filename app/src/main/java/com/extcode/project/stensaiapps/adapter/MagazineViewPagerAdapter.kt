package com.extcode.project.stensaiapps.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.screens.fragments.magazinetabs.AllMagazineFragment
import com.extcode.project.stensaiapps.screens.fragments.magazinetabs.EkskulMagazineFragment
import com.extcode.project.stensaiapps.screens.fragments.magazinetabs.KaryaMagazineFragment

class MagazineViewPagerAdapter(private val context: Context, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles = intArrayOf(
        R.string.allMagazineName,
        R.string.karyaMagazineName,
        R.string.ekskulMagazineName
    )

    private val fragment: List<Fragment> = listOf(
        AllMagazineFragment(),
        KaryaMagazineFragment(),
        EkskulMagazineFragment()
    )

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(tabTitles[position])
    }

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getCount(): Int = 3

}