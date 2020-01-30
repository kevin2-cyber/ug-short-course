/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.ui.settings

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import coil.api.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dev.ugscheduler.R
import dev.ugscheduler.databinding.FragmentSettingsBinding
import dev.ugscheduler.shared.data.Student
import dev.ugscheduler.shared.databinding.EditContentBinding
import dev.ugscheduler.shared.prefs.AppPreferences
import dev.ugscheduler.shared.util.activityViewModelProvider
import dev.ugscheduler.shared.util.debugger
import dev.ugscheduler.shared.util.toast
import dev.ugscheduler.shared.viewmodel.AppViewModel
import dev.ugscheduler.shared.viewmodel.AppViewModelFactory
import dev.ugscheduler.util.MainNavigationFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import java.util.*

class SettingsFragment : MainNavigationFragment() {

    private val viewModel by lazy { activityViewModelProvider<AppViewModel>(AppViewModelFactory(get())) }
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Observe current student
        viewModel.getCurrentStudent(false).observe(viewLifecycleOwner, Observer { student ->
            if (student != null) bindStudent(student) else debugger("Student could not be found")
        })

        // Swipe refresh setup
        binding.swipeRefresh.setOnRefreshListener {
            val currentStudent = viewModel.getCurrentStudent(true)
            currentStudent.removeObservers(viewLifecycleOwner)
            currentStudent.observe(viewLifecycleOwner, Observer { student ->
                binding.swipeRefresh.isRefreshing = false
                if (student != null) {
                    bindStudent(student)
                } else toast("Student could not be found")
            })
        }

        // Get theme preferences instance from DI
        val prefs by inject<AppPreferences>()
        binding.prefsTheme.setOnClickListener {
            val options =
                arrayOf<CharSequence>("Light", "Dark", "Set by Battery Saver", "System Default")
            MaterialAlertDialogBuilder(this@SettingsFragment.requireContext()).apply {
                setIcon(R.drawable.twotone_brightness_6_24px)
                setTitle("Select theme")
                setSingleChoiceItems(
                    options,
                    if (prefs.currentMode == -1) 3 else prefs.currentMode.minus(1)
                ) { dialog, which ->
                    dialog.dismiss()
                    uiScope.launch {
                        // Add short delay
                        delay(550)
                        //TransitionManager.beginDelayedTransition(binding.container)
                        when (which) {
                            0 -> prefs.setDarkMode(AppCompatDelegate.MODE_NIGHT_NO)
                            1 -> prefs.setDarkMode(AppCompatDelegate.MODE_NIGHT_YES)
                            2 -> prefs.setDarkMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                            else -> prefs.setDarkMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        }
                    }
                }
                setNegativeButton("Dismiss") { dialog, _ ->
                    dialog.cancel()
                }
                show()
            }
        }

        // Get user location live updates
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Extract user's address
            extractUsersLocation()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), RC_LOC_PERM
            )
        }
    }

    /// Get's the user's current location address
    private fun extractUsersLocation() {
        val geocoder = Geocoder(context, Locale.getDefault())
        ioScope.launch {
            val location =
                Tasks.await(LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation)
            if (location != null) {
                try {
                    val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)[0]
                    uiScope.launch {
                        binding.residencePrefs.summary = address.adminArea
                        binding.addressPrefs.summary = address.getAddressLine(0)
                    }
                } catch (e: Exception) {
                    debugger(e.localizedMessage)
                }

            }
        }
    }

    private fun bindStudent(student: Student) {
        binding.addressPrefs.apply {
            summary = student.address ?: "Not set"
            setOnClickListener { editContent(EditType.ADDRESS, student) }
        }
        binding.dobPrefs.apply {
            summary = student.dob ?: "Not set"
            setOnClickListener { editContent(EditType.DOB, student) }
        }
        binding.eduPrefs.apply {
            summary = student.eduBackground ?: "Not set"
            setOnClickListener { editContent(EditType.EDU, student) }
        }
        binding.orgPrefs.apply {
            summary = student.organisation ?: "Not set"
            setOnClickListener { editContent(EditType.ORG, student) }
        }
        binding.phonePrefs.apply {
            summary = student.phone ?: "Not set"
            setOnClickListener { editContent(EditType.PHONE, student) }
        }
        binding.posPrefs.apply {
            summary = student.position ?: "Not set"
            setOnClickListener { editContent(EditType.POS, student) }
        }
        binding.residencePrefs.apply {
            summary = student.residence ?: "Not set"
            setOnClickListener { editContent(EditType.RESIDENCE, student) }
        }

        binding.userName.apply {
            text = student.fullName ?: "Not set"
            setOnClickListener { editContent(EditType.USERNAME, student) }
        }
        binding.userEmail.text = student.email
        binding.userAvatar.apply {
            load(student.avatar) {
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_default_avatar_1)
                error(R.drawable.ic_default_avatar_3)
                crossfade(true)
                lifecycle(viewLifecycleOwner)
                diskCachePolicy(CachePolicy.ENABLED)
            }

            setOnClickListener { editContent(EditType.AVATAR, student) }
        }
    }

    private fun editContent(type: EditType, student: Student) {
        // Get binding
        val edtContentBinding = EditContentBinding.inflate(layoutInflater)

        // Check editable type
        when (type) {
            EditType.ADDRESS -> {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setIcon(R.drawable.twotone_map_24px)
                    setTitle("Address")
                    setView(edtContentBinding.root)
                    setPositiveButton("Save") { dialog, _ ->
                        dialog.dismiss()
                        val content = edtContentBinding.editContent.text.toString()
                        if (content.isNotEmpty()) viewModel.addStudent(student.copy(address = content))
                    }
                    setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    show()
                }
            }

            EditType.DOB -> {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setIcon(R.drawable.twotone_event_24px)
                    setTitle("Date of Birth")
                    setView(edtContentBinding.root)
                    setPositiveButton("Save") { dialog, _ ->
                        dialog.dismiss()
                        val content = edtContentBinding.editContent.text.toString()
                        if (content.isNotEmpty()) viewModel.addStudent(student.copy(dob = content))
                    }
                    setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    show()
                }
            }

            EditType.EDU -> {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setIcon(R.drawable.twotone_group_24px)
                    setTitle("Education")
                    setView(edtContentBinding.root)
                    setPositiveButton("Save") { dialog, _ ->
                        dialog.dismiss()
                        val content = edtContentBinding.editContent.text.toString()
                        if (content.isNotEmpty()) viewModel.addStudent(student.copy(eduBackground = content))
                    }
                    setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    show()
                }
            }

            EditType.ORG -> {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setIcon(R.drawable.twotone_business_center_24px)
                    setTitle("Organisation")
                    setView(edtContentBinding.root)
                    setPositiveButton("Save") { dialog, _ ->
                        dialog.dismiss()
                        val content = edtContentBinding.editContent.text.toString()
                        if (content.isNotEmpty()) viewModel.addStudent(student.copy(position = content))
                    }
                    setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    show()
                }
            }

            EditType.PHONE -> {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setIcon(R.drawable.twotone_phone_24px)
                    setTitle("Phone Number")
                    setView(edtContentBinding.root)
                    setPositiveButton("Save") { dialog, _ ->
                        dialog.dismiss()
                        val content = edtContentBinding.editContent.text.toString()
                        if (content.isNotEmpty()) viewModel.addStudent(student.copy(phone = content))
                    }
                    setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    show()
                }
            }

            EditType.POS -> {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setIcon(R.drawable.twotone_account_circle_24px)
                    setTitle("Position")
                    setView(edtContentBinding.root)
                    setPositiveButton("Save") { dialog, _ ->
                        dialog.dismiss()
                        val content = edtContentBinding.editContent.text.toString()
                        if (content.isNotEmpty()) viewModel.addStudent(student.copy(position = content))
                    }
                    setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    show()
                }
            }

            EditType.RESIDENCE -> {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setIcon(R.drawable.twotone_home_24px)
                    setTitle("Residence")
                    setView(edtContentBinding.root)
                    setPositiveButton("Save") { dialog, _ ->
                        dialog.dismiss()
                        val content = edtContentBinding.editContent.text.toString()
                        if (content.isNotEmpty()) viewModel.addStudent(student.copy(residence = content))
                    }
                    setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    show()
                }
            }

            EditType.USERNAME -> {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setIcon(R.drawable.twotone_account_circle_24px)
                    setTitle("Full Name")
                    setView(edtContentBinding.root)
                    setPositiveButton("Save") { dialog, _ ->
                        dialog.dismiss()
                        val content = edtContentBinding.editContent.text.toString()
                        if (content.isNotEmpty()) viewModel.addStudent(student.copy(fullName = content))
                    }
                    setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    show()
                }
            }

            EditType.AVATAR -> {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setIcon(R.drawable.twotone_add_a_photo_24px)
                    setTitle("Select image source")
                    setItems(arrayOf("Gallery", "Remove photo")) { dialog, which ->
                        dialog.dismiss()
                        when (which) {
                            0 -> {
                                with(Intent(Intent.ACTION_GET_CONTENT)) {
                                    setType("image/*")
                                    putExtra(
                                        Intent.EXTRA_MIME_TYPES,
                                        arrayOf("image/jpeg", "image/png", "image/jpg")
                                    )
                                    startActivityForResult(this, RC_GALLERY)
                                }
                            }

                            1 -> {
                                MaterialAlertDialogBuilder(requireContext()).apply {
                                    setIcon(R.drawable.twotone_delete_24px)
                                    setTitle("Confirm action")
                                    setMessage("Do you wish to remove your current profile picture?")
                                    setPositiveButton("Delete") { dialog, _ ->
                                        dialog.dismiss()
                                        viewModel.addStudent(student.copy(avatar = null))
                                    }
                                    setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                                    show()
                                }
                            }
                        }
                    }
                    setPositiveButton("Dismiss") { dialog, _ -> dialog.dismiss() }
                    show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GALLERY) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    // Extract image from source
                    val imageUri = data?.data
                    debugger(imageUri)

                    // Get current student
                    Snackbar.make(
                        binding.container,
                        "Uploading new profile picture...",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    viewModel.getCurrentStudent(false)
                        .observe(viewLifecycleOwner, Observer { student ->
                            // Remove old observers
                            viewModel.getCurrentStudent(false).removeObservers(viewLifecycleOwner)

                            // Update avatar
                            viewModel.addStudent(student.copy(avatar = imageUri.toString()))

                            // Upload image to server and get new url for avatar
                            uiScope.launch {
                                viewModel.uploadImage(student.id, imageUri)
                                    .observe(viewLifecycleOwner, Observer { uri ->
                                        debugger("New url => $uri")
                                        viewModel.addStudent(student.copy(avatar = uri))
                                    })
                            }
                        })
                }
            }
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
            extractUsersLocation()
    }

    companion object {
        private const val RC_GALLERY = 9
        private const val RC_LOC_PERM = 12
    }

    // Enumeration for the various editable contents
    enum class EditType { ADDRESS, DOB, EDU, ORG, POS, PHONE, RESIDENCE, USERNAME, AVATAR }
}
