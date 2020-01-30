/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.updatePaddingRelative
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.android.gms.tasks.Tasks
import com.google.maps.android.PolyUtil
import dev.ugscheduler.R
import dev.ugscheduler.databinding.MapFragmentBinding
import dev.ugscheduler.shared.BuildConfig
import dev.ugscheduler.shared.prefs.AppPreferences
import dev.ugscheduler.shared.util.activityViewModelProvider
import dev.ugscheduler.shared.util.debugger
import dev.ugscheduler.shared.util.doOnApplyWindowInsets
import dev.ugscheduler.shared.util.toLatLng
import dev.ugscheduler.shared.viewmodel.AppViewModel
import dev.ugscheduler.shared.viewmodel.AppViewModelFactory
import dev.ugscheduler.util.MainNavigationFragment
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class MapFragment : MainNavigationFragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private val viewModel: AppViewModel by lazy {
        activityViewModelProvider<AppViewModel>(
            AppViewModelFactory(get())
        )
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            // Initialize map
            map = googleMap

            val prefs = get<AppPreferences>()
            // Apply settings to map
            map.apply {
                setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        if (prefs.currentMode == AppCompatDelegate.MODE_NIGHT_YES)
                            R.raw.map_style_night else R.raw.map_style_day
                    )
                )
            }

            // Get user location live updates
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) updateUserLocationWithData() else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), RC_LOC_PERM
                )
            }
        }
    }

    // Requires permission to functions properly
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private fun updateUserLocationWithData() {
        ioScope.launch {
            // Get user's last location asynchronously
            val location =
                Tasks.await(LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation)
            if (location != null) {
                debugger("Current location is: ${location.toLatLng()}")

                uiScope.launch {
                    // Animate camera target to user's location
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(location.toLatLng(), 13f),
                        550,
                        null
                    )

                    // Add additional props to Google Map
                    with(map) {
                        // todo: update department's location
                        val department = BuildConfig.DEPARTMENT_LOCATION
                        isMyLocationEnabled = true

                        // Encode path to the department from user's location
                        val encodedPath =
                            PolyUtil.encode(mutableListOf(location.toLatLng(), department))
                        addPolyline(
                            PolylineOptions()
                                .addAll(PolyUtil.decode(encodedPath))
                                .startCap(ButtCap())
                                .endCap(RoundCap())
                        )
                    }
                    // todo: show button to help them navigate to department for lectures
                }
            } else debugger("Your last location could not be determined")
        }
    }

    private lateinit var binding: MapFragmentBinding
    private lateinit var mapViewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MapFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mapViewModel = activityViewModelProvider(ViewModelProvider.NewInstanceFactory())

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        binding.container.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePaddingRelative(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_LOC_PERM && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            updateUserLocationWithData()
    }

    companion object {
        private const val RC_LOC_PERM = 2
    }

}
