package com.example.formkpuapp.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.formkpuapp.R
import com.example.formkpuapp.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var getcoordinates: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.btnSimpanLokasi.setOnClickListener {
            if (getcoordinates != null) {
                val intent = Intent()
                intent.putExtra(EXTRA_LATITUDE, getcoordinates?.latitude)
                intent.putExtra(EXTRA_LONGITUDE, getcoordinates?.longitude)
                intent.putExtra(EXTRA_ADDRESS, getAddress(getcoordinates!!))

                Log.d("Latitude", getcoordinates?.latitude.toString())
                Log.d("Longitude", getcoordinates?.longitude.toString())
                getAddress(getcoordinates!!)?.let { it1 -> Log.d("Alamat", it1) }
                setResult(RESULT_CODE, intent)
                finish()
            } else {
                Toast.makeText(this, "Lokasi tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        }

        getMyLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true //Zoom en el mapa
        mMap.moveCamera(CameraUpdateFactory.zoomBy(17f))

        mMap.setOnMapLongClickListener { latlng ->
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng))
            getcoordinates = LatLng(latlng.latitude, latlng.longitude) //catch coordinates from
            //maps API

            val markerOptions = MarkerOptions().position(getcoordinates!!)
            val titlesr = getAddress(getcoordinates!!)
            markerOptions.title(titlesr)

            mMap.addMarker(MarkerOptions()
                .position(getcoordinates!!)
                .title(getAddress(getcoordinates!!))
                .draggable(true)
                .snippet("Lokasiku"))
        }

    }

    private fun getAddress(lat: LatLng): String? {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat.latitude, lat.longitude,1)
        return list?.get(0)?.getAddressLine(0)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLocation(){
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    showStartMarker(location)
                } else {
                    Toast.makeText(
                        this@MapsActivity,
                        "Lokasi tidak terdeteksi, Aktifkan pengaturan lokasi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showStartMarker(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(startLocation)
                .title(getString(R.string.start_point))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 17f))
    }

    companion object {
        const val EXTRA_LATITUDE = "latitude"
        const val EXTRA_LONGITUDE = "longitude"
        const val EXTRA_ADDRESS = "address"
        const val RESULT_CODE = 110
    }
}