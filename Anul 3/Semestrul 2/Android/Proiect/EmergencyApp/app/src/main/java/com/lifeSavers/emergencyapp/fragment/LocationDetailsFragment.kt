package com.lifeSavers.emergencyapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.lifeSavers.emergencyapp.R

class LocationDetailsFragment : Fragment() {
    private lateinit var nameTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var phoneTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_location_details, container, false)

        nameTextView = view.findViewById(R.id.nameTextView)
        addressTextView = view.findViewById(R.id.addressTextView)
        phoneTextView = view.findViewById(R.id.phoneTextView)

        // Get the location details from the arguments passed in
        val locationName = arguments?.getString("name")
        val locationAddress = arguments?.getString("address")
        val locationPhone = arguments?.getString("phone")

        // Set the UI element values with the location details
        nameTextView.text = locationName
        addressTextView.text = locationAddress
        phoneTextView.text = locationPhone

        return view
    }
}
