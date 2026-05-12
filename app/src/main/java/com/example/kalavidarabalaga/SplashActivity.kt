package com.example.kalavidarabalaga

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            val currentUser =
                FirebaseAuth.getInstance()
                    .currentUser

            // IF USER ALREADY LOGGED IN
            if (currentUser != null) {

                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    )
                )

            } else {

                // LOGIN REQUIRED
                startActivity(
                    Intent(
                        this,
                        LoginActivity::class.java
                    )
                )
            }

            finish()

        }, 2000)
    }
}