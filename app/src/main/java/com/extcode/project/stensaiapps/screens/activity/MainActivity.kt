package com.extcode.project.stensaiapps.screens.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.model.StudentModel
import com.extcode.project.stensaiapps.model.TeacherModel
import com.extcode.project.stensaiapps.other.*
import com.extcode.project.stensaiapps.screens.fragments.DashboardFragment
import com.extcode.project.stensaiapps.screens.fragments.MagazineFragment
import com.extcode.project.stensaiapps.screens.fragments.PeranFragment
import com.extcode.project.stensaiapps.screens.fragments.TaskFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(topAppBar)

        var userName: String
        var userClass: String
        var idStatus: Int

        getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).apply {
            idStatus = getInt(kIdStatus, 0)
            userName = getString(kUserName, "").toString()
            userClass = getString(kUserClass, "").toString()
        }

        getNameFromDatabase(userName, idStatus)

        val nameUser = userName.split(" ").toTypedArray()
        name = if (idStatus == 0) "${nameUser[0]} - $userClass" else nameUser[0]

        navigationChange(DashboardFragment(), name)

        bottomNavigationContainer.setNavigationChangeListener { _, position ->
            when (position) {
                0 -> navigationChange(DashboardFragment(), name)
                1 -> navigationChange(TaskFragment(), getString(R.string.tugas))
                2 -> navigationChange(MagazineFragment(), getString(R.string.mading))
                3 -> navigationChange(PeranFragment(), getString(R.string.peran))
            }
        }

    }

    private fun getNameFromDatabase(userName: String, idStatus: Int) {

        if (userName.isEmpty()) {
            isLoading(true)
            val uid = FirebaseAuth.getInstance().uid
            val status = if (idStatus == 0) "students" else "teachers"

            val ref = FirebaseDatabase.getInstance().getReference("/users/$status/$uid")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (idStatus == 0) {
                        val user = snapshot.getValue(StudentModel::class.java)
                        getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).apply {
                            edit {
                                if (user != null) {
                                    putString(kUserName, user.username)
                                    putLong(kUserNIS, user.nis!!)
                                    putString(kUserClass, user.className)
                                    apply()
                                }
                            }
                        }
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        isLoading(false)
                    } else {
                        val user = snapshot.getValue(TeacherModel::class.java)
                        getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).apply {
                            edit {
                                putString(kUserName, user!!.username)
                                putLong(kUserNIP, user.nip!!)
                                apply()
                            }
                        }
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        isLoading(false)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    isLoading(false)
                    Toast.makeText(
                        this@MainActivity,
                        "gagal mengambil data, $error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.setting -> {
           /* FirebaseAuth.getInstance().signOut()
            getSharedPreferences(SignInActivity::class.simpleName, MODE_PRIVATE).apply {
                edit {
                    clear()
                    commit()
                }
            }
            startActivity(Intent(this, SignInActivity::class.java))
            finish()*/
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


    private fun isLoading(bool: Boolean) {
        if (bool) {
            progressBar.visibility = View.VISIBLE
            loadingBackground.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            loadingBackground.visibility = View.GONE
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