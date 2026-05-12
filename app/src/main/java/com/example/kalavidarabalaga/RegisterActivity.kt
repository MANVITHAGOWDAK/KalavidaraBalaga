package com.example.kalavidarabalaga

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        // VIEWS
        val firstName = findViewById<EditText>(R.id.firstName)
        val middleName = findViewById<EditText>(R.id.middleName)
        val lastName = findViewById<EditText>(R.id.lastName)
        val dob = findViewById<EditText>(R.id.dob)
        val phone = findViewById<EditText>(R.id.phone)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val confirmPassword = findViewById<EditText>(R.id.confirmPassword)

        val showRegisterPassword =
            findViewById<CheckBox>(R.id.showRegisterPassword)

        val passwordStatus =
            findViewById<TextView>(R.id.passwordStatus)

        val registerBtn =
            findViewById<Button>(R.id.registerBtn)

        val roleSpinner =
            findViewById<Spinner>(R.id.roleSpinner)

        // ROLE LIST
        val roles = arrayOf(
            "Artist",
            "Organizer",
            "User"
        )

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            roles
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        roleSpinner.adapter = adapter

        // DATE PICKER
        val calendar = Calendar.getInstance()

        dob.setOnClickListener {

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->

                    val date =
                        "$selectedDay/${selectedMonth + 1}/$selectedYear"

                    dob.setText(date)

                },
                year,
                month,
                day
            )

            datePicker.show()
        }

        // SHOW PASSWORD
        showRegisterPassword.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {

                password.inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

                confirmPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

            } else {

                password.inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_PASSWORD

                confirmPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            password.setSelection(password.text.length)
            confirmPassword.setSelection(confirmPassword.text.length)
        }

        // PASSWORD MATCH CHECK
        confirmPassword.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                val pass =
                    password.text.toString()

                val confirm =
                    confirmPassword.text.toString()

                if (confirm.isEmpty()) {

                    passwordStatus.text = ""

                } else if (pass == confirm) {

                    passwordStatus.text =
                        "✓ Passwords Match"

                    passwordStatus.setTextColor(Color.GREEN)

                } else {

                    passwordStatus.text =
                        "✗ Passwords Do Not Match"

                    passwordStatus.setTextColor(Color.RED)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // REGISTER BUTTON
        registerBtn.setOnClickListener {

            val firstNameText =
                firstName.text.toString().trim()

            val middleNameText =
                middleName.text.toString().trim()

            val lastNameText =
                lastName.text.toString().trim()

            val dobText =
                dob.text.toString().trim()

            val phoneText =
                phone.text.toString().trim()

            val emailText =
                email.text.toString().trim()

            val passwordText =
                password.text.toString().trim()

            val confirmPasswordText =
                confirmPassword.text.toString().trim()

            val role =
                roleSpinner.selectedItem.toString()

            // REQUIRED VALIDATION
            if (
                firstNameText.isEmpty() ||
                dobText.isEmpty() ||
                phoneText.isEmpty() ||
                emailText.isEmpty() ||
                passwordText.isEmpty() ||
                confirmPasswordText.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Please fill all required fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // PASSWORD MATCH
            if (passwordText != confirmPasswordText) {

                Toast.makeText(
                    this,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // STRONG PASSWORD
            val passwordPattern =
                Regex(
                    "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{6,}$"
                )

            if (!passwordPattern.matches(passwordText)) {

                Toast.makeText(
                    this,
                    "Password must contain uppercase, lowercase, number and symbol",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            // FIREBASE REGISTER
            auth.createUserWithEmailAndPassword(
                emailText,
                passwordText
            ).addOnCompleteListener {

                if (it.isSuccessful) {

                    // EMAIL VERIFICATION
                    auth.currentUser
                        ?.sendEmailVerification()

                    val uid =
                        auth.currentUser!!.uid

                    val userMap =
                        HashMap<String, String>()

                    userMap["firstName"] =
                        firstNameText

                    userMap["middleName"] =
                        middleNameText

                    userMap["lastName"] =
                        lastNameText

                    userMap["dob"] =
                        dobText

                    userMap["phone"] =
                        phoneText

                    userMap["email"] =
                        emailText

                    userMap["role"] =
                        role

                    FirebaseDatabase.getInstance()
                        .getReference("users")
                        .child(uid)
                        .setValue(userMap)

                    Toast.makeText(
                        this,
                        "Registration Successful.\nVerification Email Sent.",
                        Toast.LENGTH_LONG
                    ).show()

                    FirebaseAuth.getInstance()
                        .signOut()

                    startActivity(
                        Intent(
                            this,
                            LoginActivity::class.java
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
    }
}