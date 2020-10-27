package com.extcode.project.stensaiapps.screens.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) intentPostDelayed(SignInActivity::class.java) else intentPostDelayed(
            MainActivity::class.java
        )
    }

    private fun intentPostDelayed(cls: Class<*>) {
        Handler().postDelayed({
            startActivity(Intent(this, cls))
            finish()
        }, 1000L)
    }
}