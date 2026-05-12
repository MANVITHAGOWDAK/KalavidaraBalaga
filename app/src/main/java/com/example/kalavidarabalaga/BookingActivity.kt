package com.example.kalavidarabalaga

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_booking)

        val eventName =
            findViewById<EditText>(R.id.eventName)

        val eventLocation =
            findViewById<EditText>(R.id.eventLocation)

        val eventDate =
            findViewById<EditText>(R.id.eventDate)

        val confirmBtn =
            findViewById<Button>(R.id.confirmBookingBtn)

        confirmBtn.setOnClickListener {

            val ref =
                FirebaseDatabase.getInstance()
                    .getReference("bookings")

            val bookingId =
                ref.push().key!!

            val user =
                FirebaseAuth.getInstance()
                    .currentUser

            val booking = Booking(

                bookingId,

                "",

                "",

                user?.uid,

                user?.email,

                eventName.text.toString(),

                eventLocation.text.toString(),

                eventDate.text.toString(),

                "Pending"
            )

            ref.child(bookingId)
                .setValue(booking)

            Toast.makeText(
                this,
                "Booking Sent",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}