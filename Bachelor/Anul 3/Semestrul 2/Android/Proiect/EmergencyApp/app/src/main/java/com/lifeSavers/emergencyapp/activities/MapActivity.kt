package com.lifeSavers.emergencyapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lifeSavers.emergencyapp.R
import com.lifeSavers.emergencyapp.fragment.LocationDetailsFragment
import com.lifeSavers.emergencyapp.utils.Utils
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mancj.materialsearchbar.MaterialSearchBar.OnSearchActionListener
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import com.skyfishjy.library.RippleBackground

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    lateinit var placesClient: PlacesClient
    lateinit var predictionList: List<AutocompletePrediction>
    lateinit var materialSearchBar: MaterialSearchBar
    private var mapView: View? = null
    private lateinit var rippleBg: RippleBackground
    val defaultZoom = 15f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST) {
        }

        setContentView(R.layout.activity_map)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

        val db =
            FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
        val dbCollection = db.getReference("Users")
        dbCollection.child(firebaseUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email = "${snapshot.child("email").value}"
                    val name = "${snapshot.child("name").value}"

                    val nameTextView: TextView = findViewById(R.id.user_name)
                    nameTextView.text = name
                    val emailTextView: TextView = findViewById(R.id.user_email)
                    emailTextView.text = email
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_assistants -> {
                    startActivity(Intent(this, AssistantsListForUsersActivity::class.java))
                }
                R.id.nav_map -> {
                    startActivity(Intent(this, PermissionsActivity::class.java))
                }
                R.id.nav_urgent_call -> {
                    startActivity(Intent(this, EmergencyPhoneNumbersActivity::class.java))
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
                R.id.nav_logout -> {
                    firebaseAuth.signOut()
                    startActivity(Intent(this, LogInActivity::class.java))
                }
                R.id.nav_share_app -> {
                    Utils().shareButtonFunctionality(this)
                }
                R.id.nav_show_guide -> {
                    startActivity(Intent(this, GuidePage1::class.java))
                }
                R.id.nav_show_tutorial_video -> {
                    startActivity(Intent(this, TutorialVideoActivity::class.java))
                }
            }
            true
        }

        materialSearchBar = findViewById(R.id.searchBar)
        rippleBg = findViewById(R.id.ripple_bg)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        mapView = mapFragment.view
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@MapActivity)

        val appInfo = applicationContext.packageManager.getApplicationInfo(
            applicationContext.packageName,
            PackageManager.GET_META_DATA
        )
        val apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY")
        Places.initialize(this@MapActivity, apiKey!!)
        placesClient = Places.createClient(this)

        val token = AutocompleteSessionToken.newInstance()

        materialSearchBar.setOnSearchActionListener(object : OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {}

            override fun onSearchConfirmed(text: CharSequence) {
                val searchQuery = text.toString()

                val request = FindAutocompletePredictionsRequest.builder()
                    .setTypeFilter(TypeFilter.ADDRESS)
                    .setSessionToken(token)
                    .setQuery(searchQuery)
                    .build()

                placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                    val predictionList = response.autocompletePredictions
                    updateSearchBarSuggestions(predictionList)
                }.addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        Log.e("MapActivity", exception.message.toString())
                    }
                }
            }

            override fun onButtonClicked(buttonCode: Int) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    } else {
                        drawerLayout.openDrawer(GravityCompat.START)
                    }
                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    materialSearchBar.disableSearch()
                }
            }
        })
        materialSearchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val predictionsRequest = FindAutocompletePredictionsRequest.builder()
                    .setTypeFilter(TypeFilter.ADDRESS)
                    .setSessionToken(token)
                    .setQuery(s.toString())
                    .build()
                placesClient.findAutocompletePredictions(predictionsRequest)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val predictionsResponse = task.result
                            if (predictionsResponse != null) {
                                predictionList = predictionsResponse.autocompletePredictions
                                val suggestionsList: MutableList<String?> =
                                    ArrayList()
                                for (i in predictionList.indices) {
                                    val prediction = predictionList[i]
                                    suggestionsList.add(prediction.getFullText(null).toString())
                                }
                                materialSearchBar.updateLastSuggestions(suggestionsList)
                                if (!materialSearchBar.isSuggestionsVisible) {
                                    materialSearchBar.showSuggestionsList()
                                }
                            }
                        } else {
                            Log.i("myTag", "prediction fetching task unsuccessful")
                        }
                    }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        materialSearchBar.setSuggestionsClickListener(object :
            SuggestionsAdapter.OnItemViewClickListener {
            override fun OnItemClickListener(position: Int, v: View) {
                if (position >= predictionList.size) {
                    return
                }
                val selectedPrediction = predictionList[position]
                val suggestion = materialSearchBar.lastSuggestions[position].toString()
                materialSearchBar.text = suggestion
                Handler(Looper.getMainLooper()).postDelayed(
                    { materialSearchBar.clearSuggestions() },
                    1000
                )
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(
                    materialSearchBar.windowToken,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
                val placeId = selectedPrediction.placeId
                val placeFields = listOf(
                    Place.Field.LAT_LNG,
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.PHONE_NUMBER
                )
                val fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build()
                placesClient.fetchPlace(fetchPlaceRequest)
                    .addOnSuccessListener { fetchPlaceResponse ->
                        val place = fetchPlaceResponse.place
                        val bundle = Bundle()
                        bundle.putString("name", place.name)
                        bundle.putString("address", place.address)
                        bundle.putString("phone", place.phoneNumber)

                        val locationDetailsFragment = LocationDetailsFragment()
                        locationDetailsFragment.arguments = bundle

                        supportFragmentManager.beginTransaction()
                            .replace(R.id.locationDetailsFragmentContainer, locationDetailsFragment)
                            .addToBackStack(null)
                            .commit()

                        val latLngOfPlace = place.latLng
                        if (latLngOfPlace != null) {
                            mMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    latLngOfPlace,
                                    defaultZoom
                                )
                            )
                        }
                    }.addOnFailureListener { e ->
                        if (e is ApiException) {
                            e.printStackTrace()
                            val statusCode = e.statusCode
                            Log.i("myTag", "place not found: " + e.message)
                            Log.i("myTag", "status code: $statusCode")
                        }
                    }
            }

            override fun OnItemDeleteListener(position: Int, v: View) {}
        })
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.isIndoorEnabled = true
        mMap.isBuildingsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        if (mapView != null && mapView!!.findViewById<View?>("1".toInt()) != null) {
            val locationButton =
                (mapView!!.findViewById<View>("1".toInt()).parent as View).findViewById<View>("2".toInt())
            val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
            locationButton.bringToFront()
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.setMargins(350, 0, 0, 400)
        }

        //check if gps is enabled or not and then request user to enable it
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this@MapActivity)
        val task = settingsClient.checkLocationSettings(builder.build())
        task.addOnSuccessListener(
            this@MapActivity
        ) {
        }
        task.addOnFailureListener(
            this@MapActivity
        ) { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this@MapActivity, 51)
                } catch (e1: SendIntentException) {
                    e1.printStackTrace()
                }
            }
        }
        mMap.setOnMyLocationButtonClickListener {
            if (materialSearchBar.isSuggestionsVisible) materialSearchBar.clearSuggestions()
            if (materialSearchBar.isSearchEnabled) materialSearchBar.disableSearch()
            false
        }
    }

    private fun updateSearchBarSuggestions(predictions: List<AutocompletePrediction>) {
        val suggestionList = ArrayList<String>()
        for (prediction in predictions) {
            suggestionList.add(prediction.getFullText(null).toString())
        }

        materialSearchBar.updateLastSuggestions(suggestionList)
        if (!materialSearchBar.isSuggestionsVisible) {
            materialSearchBar.showSuggestionsList()
        }
    }
}
