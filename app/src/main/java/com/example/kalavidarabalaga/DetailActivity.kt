package com.example.kalavidarabalaga

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        // IDS
        val imageView =
            findViewById<ImageView>(R.id.detailImage)

        val nameText =
            findViewById<TextView>(R.id.detailName)

        val artText =
            findViewById<TextView>(R.id.detailArt)

        val locationText =
            findViewById<TextView>(R.id.detailLocation)

        val phoneText =
            findViewById<TextView>(R.id.detailPhone)

        val equipmentText =
            findViewById<TextView>(R.id.detailEquipment)

        val callBtn =
            findViewById<Button>(R.id.callBtn)

        val smsBtn =
            findViewById<Button>(R.id.smsBtn)

        val whatsappBtn =
            findViewById<Button>(R.id.whatsappBtn)

        val emailBtn =
            findViewById<Button>(R.id.emailBtn)

        val bookingBtn =
            findViewById<Button>(R.id.confirmBookingBtn)

        val editBtn =
            findViewById<Button>(R.id.editBtn)

        val deleteBtn =
            findViewById<Button>(R.id.deleteBtn)

        // FEEDBACK
        val ratingBar =
            findViewById<RatingBar>(R.id.ratingBar)

        val feedbackBox =
            findViewById<EditText>(R.id.feedbackBox)

        val submitFeedbackBtn =
            findViewById<Button>(R.id.submitFeedbackBtn)

        // GET DATA
        val id =
            intent.getStringExtra("id") ?: ""

        val name =
            intent.getStringExtra("name") ?: ""

        val art =
            intent.getStringExtra("art") ?: ""

        val location =
            intent.getStringExtra("location") ?: ""

        val phone =
            intent.getStringExtra("phone") ?: ""

        val imageUrl =
            intent.getStringExtra("imageUrl") ?: ""

        val equipment =
            intent.getStringExtra("equipment") ?: ""

        val ownerId =
            intent.getStringExtra("ownerId") ?: ""

        // SET DATA
        nameText.text = name
        artText.text = art
        locationText.text = location
        phoneText.text = phone
        equipmentText.text = "Equipment: $equipment"

        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        // CALL
        callBtn.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_DIAL
            )

            intent.data =
                Uri.parse("tel:$phone")

            startActivity(intent)
        }

        // SMS
        smsBtn.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_VIEW
            )

            intent.data =
                Uri.parse("sms:$phone")

            intent.putExtra(
                "sms_body",
                "Hello $name, I want to book your performance."
            )

            startActivity(intent)
        }

        // WHATSAPP
        whatsappBtn.setOnClickListener {

            val url =
                "https://wa.me/91$phone"

            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )

            startActivity(intent)
        }

        // EMAIL
        emailBtn.setOnClickListener {

            val intent =
                Intent(Intent.ACTION_SEND)

            intent.type = "message/rfc822"

            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                "Artist Booking"
            )

            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Hello $name, I want to book your performance."
            )

            startActivity(
                Intent.createChooser(
                    intent,
                    "Send Email"
                )
            )
        }

        // BOOKING
        bookingBtn.setOnClickListener {

            val bookingRef =
                FirebaseDatabase.getInstance()
                    .getReference("bookings")

            val bookingId =
                bookingRef.push().key!!

            val booking = Booking(

                bookingId,
                name,
                "Event",
                "Reason",
                location,
                "01/01/2026",
                phone,
                "User",
                "Pending"
            )

            bookingRef.child(bookingId)
                .setValue(booking)

            Toast.makeText(
                this,
                "Booking Sent",
                Toast.LENGTH_SHORT
            ).show()
        }

        // OWNER CHECK
        val currentUser =
            FirebaseAuth.getInstance().currentUser

        if (currentUser != null &&
            currentUser.uid == ownerId
        ) {

            editBtn.visibility = Button.VISIBLE
            deleteBtn.visibility = Button.VISIBLE

        } else {

            editBtn.visibility = Button.GONE
            deleteBtn.visibility = Button.GONE
        }

        // EDIT
        editBtn.setOnClickListener {

            val intent =
                Intent(
                    this,
                    EditArtistActivity::class.java
                )

            intent.putExtra("id", id)
            intent.putExtra("name", name)
            intent.putExtra("art", art)
            intent.putExtra("location", location)
            intent.putExtra("phone", phone)
            intent.putExtra("imageUrl", imageUrl)
            intent.putExtra("equipment", equipment)

            startActivity(intent)
        }

        // DELETE
        deleteBtn.setOnClickListener {

            FirebaseDatabase.getInstance()
                .getReference("artists")
                .child(id)
                .removeValue()

            Toast.makeText(
                this,
                "Artist Deleted",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }

        // FEEDBACK SUBMIT
        submitFeedbackBtn.setOnClickListener {

            val feedbackRef =
                FirebaseDatabase.getInstance()
                    .getReference("feedback")

            val feedbackId =
                feedbackRef.push().key!!

            val feedback = Feedback(

                "User",

                feedbackBox.text.toString(),

                ratingBar.rating
            )

            feedbackRef.child(feedbackId)
                .setValue(feedback)

            Toast.makeText(
                this,
                "Feedback Submitted",
                Toast.LENGTH_SHORT
            ).show()

            feedbackBox.text.clear()

            ratingBar.rating = 0f
        }
    }
}