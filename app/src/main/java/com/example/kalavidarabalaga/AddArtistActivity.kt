package com.example.kalavidarabalaga

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddArtistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_artist)

        val name =
            findViewById<EditText>(R.id.name)

        val art =
            findViewById<EditText>(R.id.art)

        val location =
            findViewById<EditText>(R.id.location)

        val district =
            findViewById<EditText>(R.id.district)

        val phone =
            findViewById<EditText>(R.id.phone)

        val imageUrl =
            findViewById<EditText>(R.id.imageUrl)

        val equipment =
            findViewById<EditText>(R.id.equipment)

        val save = findViewById<Button>(R.id.save)

        save.setOnClickListener {

            val ref =
                FirebaseDatabase.getInstance()
                    .getReference("artists")

            val id =
                ref.push().key!!

            val ownerId =
                FirebaseAuth.getInstance()
                    .currentUser?.uid

            val artist =
                Artist(
                    id,
                    name.text.toString(),
                    art.text.toString(),
                    location.text.toString(),
                    district.text.toString(),
                    phone.text.toString(),
                    imageUrl.text.toString(),
                    equipment.text.toString(),
                    ownerId
                )

            ref.child(id)
                .setValue(artist)

            Toast.makeText(
                this,
                "Artist Added Successfully",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}