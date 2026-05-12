package com.example.kalavidarabalaga

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class EditArtistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_artist)

        val name = findViewById<EditText>(R.id.name)
        val art = findViewById<EditText>(R.id.art)
        val location = findViewById<EditText>(R.id.location)
        val phone = findViewById<EditText>(R.id.phone)
        val imageUrl = findViewById<EditText>(R.id.imageUrl)
        val gallery = findViewById<EditText>(R.id.gallery)
        val equipment = findViewById<EditText>(R.id.equipment)

        val save = findViewById<Button>(R.id.save)

        // GET DATA
        val artistId = intent.getStringExtra("id")

        name.setText(intent.getStringExtra("name"))
        art.setText(intent.getStringExtra("art"))
        location.setText(intent.getStringExtra("location"))
        phone.setText(intent.getStringExtra("phone"))
        imageUrl.setText(intent.getStringExtra("imageUrl"))
        gallery.setText(intent.getStringExtra("gallery"))
        equipment.setText(intent.getStringExtra("equipment"))

        // UPDATE
        save.text = "Update Artist"

        save.setOnClickListener {

            val updatedArtist = Artist(

                artistId,

                name.text.toString(),

                art.text.toString(),

                location.text.toString(),

                phone.text.toString(),

                imageUrl.text.toString(),

                gallery.text.toString(),

                equipment.text.toString(),

                intent.getStringExtra("ownerId")
            )

            FirebaseDatabase.getInstance()
                .getReference("artists")
                .child(artistId!!)
                .setValue(updatedArtist)

            Toast.makeText(
                this,
                "Artist Updated Successfully",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}