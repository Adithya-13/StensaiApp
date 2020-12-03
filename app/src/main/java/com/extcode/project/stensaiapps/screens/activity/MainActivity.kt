package com.extcode.project.stensaiapps.screens.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.other.kIdStatus
import com.extcode.project.stensaiapps.other.kUserName
import com.extcode.project.stensaiapps.screens.fragments.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(topAppBar)

        var userName: String
        var idStatus: Int

        getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).apply {
            idStatus = getInt(kIdStatus, 0)
            userName = getString(kUserName, "").toString()
            Log.d("njay", userName)
        }

        val nameUser = userName.split(" ").toTypedArray()
        name = nameUser[0]

        navigationChange(
            if (idStatus == 0) DashboardFragment() else TeacherDashboardFragment(),
            name
        )

        bottomNavigationContainer.setNavigationChangeListener { _, position ->
            when (position) {
                0 -> navigationChange(
                    if (idStatus == 0) DashboardFragment() else TeacherDashboardFragment(),
                    name
                )
                1 -> navigationChange(
                    TaskFragment(),
                    if (idStatus == 0) getString(R.string.tugas) else getString(R.string.ransel)
                )
                2 -> navigationChange(MagazineFragment(), getString(R.string.mading))
                3 -> navigationChange(PeranFragment(), getString(R.string.peran))
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.setting -> {
            startActivity(Intent(this, SettingActivity::class.java))
            true
        }
        R.id.chat -> {
            startActivity(Intent(this, LatestMessagesActivity::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun navigationChange(fragment: Fragment, title: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

        topAppBar.title = title
    }

}