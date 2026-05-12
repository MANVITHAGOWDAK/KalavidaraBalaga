package com.example.kalavidarabalaga

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class BookingAdapter(

    private val list: ArrayList<Booking>

) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val eventName =
            itemView.findViewById<TextView>(R.id.eventName)

        val location =
            itemView.findViewById<TextView>(R.id.eventLocation)

        val date =
            itemView.findViewById<TextView>(R.id.eventDate)

        val status =
            itemView.findViewById<TextView>(R.id.bookingStatus)

        val acceptBtn =
            itemView.findViewById<Button>(R.id.acceptBtn)

        val rejectBtn =
            itemView.findViewById<Button>(R.id.rejectBtn)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_booking,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val booking = list[position]

        holder.eventName.text =
            "Event: ${booking.eventName}"

        holder.location.text =
            "Location: ${booking.eventLocation}"

        holder.date.text =
            "Date: ${booking.eventDate}"

        holder.status.text =
            "Status: ${booking.status}"

        // ACCEPT
        holder.acceptBtn.setOnClickListener {

            FirebaseDatabase.getInstance()
                .getReference("bookings")
                .child(booking.bookingId!!)
                .child("status")
                .setValue("Accepted")

            Toast.makeText(
                holder.itemView.context,
                "Booking Accepted",
                Toast.LENGTH_SHORT
            ).show()
        }

        // REJECT
        holder.rejectBtn.setOnClickListener {

            FirebaseDatabase.getInstance()
                .getReference("bookings")
                .child(booking.bookingId!!)
                .child("status")
                .setValue("Rejected")

            Toast.makeText(
                holder.itemView.context,
                "Booking Rejected",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int {

        return list.size
    }
}