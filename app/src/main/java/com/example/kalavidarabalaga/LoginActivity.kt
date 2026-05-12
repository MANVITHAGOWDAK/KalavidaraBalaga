package com.example.kalavidarabalaga

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)

        val showPassword =
            findViewById<CheckBox>(R.id.showPassword)

        val loginBtn =
            findViewById<Button>(R.id.loginBtn)

        val forgotPassword =
            findViewById<TextView>(R.id.forgotPassword)

        val registerText =
            findViewById<TextView>(R.id.registerText)

        // SHOW PASSWORD
        showPassword.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {

                password.inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

            } else {

                password.inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            password.setSelection(password.text.length)
        }

        // LOGIN
        loginBtn.setOnClickListener {

            val emailText =
                email.text.toString().trim()

            val passwordText =
                password.text.toString().trim()

            if (emailText.isEmpty() ||
                passwordText.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(
                emailText,
                passwordText
            ).addOnCompleteListener {

                if (it.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Login Successful",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        )
                    )

                    finish()

                } else {

                    Toast.makeText(
                        this,
                        it.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // FORGOT PASSWORD
        forgotPassword.setOnClickListener {

            val emailText =
                email.text.toString().trim()

            if (emailText.isEmpty()) {

                Toast.makeText(
                    this,
                    "Enter email first",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(emailText)
                .addOnCompleteListener {

                    if (it.isSuccessful) {

                        Toast.makeText(
                            this,
                            "Reset password email sent",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        Toast.makeText(
                            this,
                            it.exception?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // OPEN REGISTER
        registerText.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }
    }
}