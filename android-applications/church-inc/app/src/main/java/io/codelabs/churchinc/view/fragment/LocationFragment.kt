package io.codelabs.churchinc.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.codelabs.churchinc.R
import io.codelabs.churchinc.core.RootFragment

class LocationFragment : RootFragment(), OnMapReadyCallback {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        // Location of church
        val churchLocation = LatLng(-34.0, 151.0)   //

        // Add marker to location
        googleMap?.addMarker(MarkerOptions().position(churchLocation).title(getString(R.string.app_name)))

        // Move camera to location
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(churchLocation, 16.0f))
    }
}