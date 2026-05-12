package com.example.kalavidarabalaga

data class Booking(

    var bookingId: String? = "",

    var artistId: String? = "",

    var artistName: String? = "",

    var userId: String? = "",

    var userEmail: String? = "",

    var eventName: String? = "",

    var eventLocation: String? = "",

    var eventDate: String? = "",

    var status: String? = "Pending"
)