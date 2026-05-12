package com.example.kalavidarabalaga

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBox: EditText
    private lateinit var filterSpinner: Spinner
    private lateinit var addBtn: Button
    private lateinit var logoutBtn: Button
    private lateinit var noDataText: TextView

    private lateinit var artistList: ArrayList<Artist>
    private lateinit var filteredList: ArrayList<Artist>
    private lateinit var adapter: ArtistAdapter

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // IDS
        recyclerView = findViewById(R.id.recyclerView)
        searchBox = findViewById(R.id.searchBox)
        filterSpinner = findViewById(R.id.filterSpinner)
        addBtn = findViewById(R.id.addBtn)
        logoutBtn = findViewById(R.id.logoutBtn)
        noDataText = findViewById(R.id.noDataText)

        // RECYCLER VIEW
        recyclerView.layoutManager =
            LinearLayoutManager(this)

        artistList = ArrayList()
        filteredList = ArrayList()

        adapter = ArtistAdapter(filteredList) { artist ->

            val intent =
                Intent(this, DetailActivity::class.java)

            intent.putExtra("id", artist.id)
            intent.putExtra("name", artist.name)
            intent.putExtra("art", artist.art)
            intent.putExtra("location", artist.location)
            intent.putExtra("phone", artist.phone)
            intent.putExtra("imageUrl", artist.imageUrl)
            intent.putExtra("equipment", artist.equipment)
            intent.putExtra("ownerId", artist.ownerId)

            startActivity(intent)
        }

        recyclerView.adapter = adapter

        // FIREBASE
        database = FirebaseDatabase.getInstance()
            .getReference("artists")

        loadArtists()

        // FILTER OPTIONS
        val filterOptions = arrayOf(
            "All",
            "Dollu Kunitha",
            "Pooja Kunitha",
            "Yakshagana",
            "Veeragase"
        )

        // CUSTOM SPINNER ADAPTER
        val spinnerAdapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            filterOptions
        ) {

            override fun getView(
                position: Int,
                convertView: View?,
                parent: android.view.ViewGroup
            ): View {

                val view =
                    super.getView(
                        position,
                        convertView,
                        parent
                    )

                val text =
                    view.findViewById<TextView>(
                        android.R.id.text1
                    )

                text.setTextColor(Color.BLACK)

                text.textSize = 18f

                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: android.view.ViewGroup
            ): View {

                val view =
                    super.getDropDownView(
                        position,
                        convertView,
                        parent
                    )

                val text =
                    view.findViewById<TextView>(
                        android.R.id.text1
                    )

                text.setTextColor(Color.BLACK)

                text.textSize = 18f

                return view
            }
        }

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        filterSpinner.adapter = spinnerAdapter

        // SEARCH
        searchBox.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                filterArtists()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // FILTER SPINNER
        filterSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    filterArtists()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        // SHOW ADD BUTTON
        addBtn.visibility = View.VISIBLE

        // ADD ARTIST
        addBtn.setOnClickListener {

            val intent =
                Intent(
                    this@MainActivity,
                    AddArtistActivity::class.java
                )

            startActivity(intent)
        }

        // LOGOUT
        logoutBtn.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finish()
        }
    }

    // LOAD ARTISTS
    private fun loadArtists() {

        database.addValueEventListener(
            object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    artistList.clear()

                    for (data in snapshot.children) {

                        val artist =
                            data.getValue(Artist::class.java)

                        if (artist != null) {

                            artistList.add(artist)
                        }
                    }

                    filterArtists()
                }

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(
                        this@MainActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // FILTER FUNCTION
    private fun filterArtists() {

        val searchText =
            searchBox.text.toString().trim()

        val selectedFilter =
            filterSpinner.selectedItem.toString()

        filteredList.clear()

        for (artist in artistList) {

            val matchesSearch =

                artist.name!!.contains(
                    searchText,
                    true
                ) ||

                        artist.art!!.contains(
                            searchText,
                            true
                        ) ||

                        artist.location!!.contains(
                            searchText,
                            true
                        )

            val matchesFilter =

                selectedFilter == "All" ||

                        artist.art.equals(
                            selectedFilter,
                            true
                        )

            if (matchesSearch && matchesFilter) {

                filteredList.add(artist)
            }
        }

        adapter.notifyDataSetChanged()

        if (filteredList.isEmpty()) {

            noDataText.visibility = View.VISIBLE

        } else {

            noDataText.visibility = View.GONE
        }
    }
}