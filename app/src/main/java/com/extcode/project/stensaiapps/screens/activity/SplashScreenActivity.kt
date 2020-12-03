package com.extcode.project.stensaiapps.screens.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.extcode.project.stensaiapps.other.kHasLoggedIn

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hasLoggedIn =
            getSharedPreferences(SignInActivity::class.simpleName, Context.MODE_PRIVATE).getBoolean(
                kHasLoggedIn, false
            )

        if (hasLoggedIn) intentPostDelayed(
            MainActivity::class.java
        ) else intentPostDelayed(SignInActivity::class.java)
    }

    private fun intentPostDelayed(cls: Class<*>) {
        Handler().postDelayed({
            startActivity(Intent(this, cls))
            finish()
        }, 1000L)
    }
}