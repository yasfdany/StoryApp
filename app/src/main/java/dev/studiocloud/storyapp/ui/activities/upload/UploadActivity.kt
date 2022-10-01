@file:Suppress("DEPRECATION")

package dev.studiocloud.storyapp.ui.activities.upload

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.databinding.ActivityUploadBinding
import dev.studiocloud.storyapp.ui.components.OnTextChange
import dev.studiocloud.storyapp.utils.Tools
import dev.studiocloud.storyapp.viewModel.StoryViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var progressDialog: ProgressDialog

    private lateinit var viewModelFactory: ViewModelFactory
    private var storyViewModel: StoryViewModel? = null
    private var selectedImage: Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val startForProfileImageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                selectedImage = fileUri
                binding.ivSelectedImage.setImageURI(fileUri)
                binding.buttonAdd.enable = binding.edAddDescription.getText().isNotEmpty() && selectedImage != null
            }
        }
    private val requestStoragePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        val isAllPermissionsGranted = permission.entries.filter { it.value }.size == 2
        if (isAllPermissionsGranted) pickImage()
    }
    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {
                }
            }
        }

    private fun obtainStoryViewModel(): StoryViewModel {
        viewModelFactory = ViewModelFactory.getInstance(this)
        return ViewModelProvider(this, viewModelFactory)[StoryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this, R.style.AppCompatAlertDialogStyle)
        progressDialog.setMessage(getString(R.string.loading))
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        storyViewModel = obtainStoryViewModel()

        setupViews()
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getMyLastLocation() {
        progressDialog.show()

        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    postStory(LatLng(location.latitude,location.latitude))
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@UploadActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestLocationPermissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    private fun pickImage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkPermission(Manifest.permission.CAMERA) &&
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            ){
                ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            } else {
                requestStoragePermissionLauncher.launch(arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ))
            }
        }
    }

    private fun postStory(latLng: LatLng){
        Tools().hideKeyboard(this)

        storyViewModel?.postNewStory(
            selectedImage,
            binding.edAddDescription.getText(),
            latLng,
            onSuccess = {
                progressDialog.hide()
                Toast.makeText(this, it?.message, Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            },
            onFailed = {
                progressDialog.hide()
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setupViews() {
        with(binding){
            buttonAdd.enable = false
            edAddDescription.addOnTextChange(object: OnTextChange{
                override fun onChange(text: String) {
                    buttonAdd.enable = text.isNotEmpty() && selectedImage != null
                }
            })
            ibBack.setOnClickListener { finish() }
            llPickPhoto.setOnClickListener { pickImage() }
            buttonAdd.setOnClickListener { getMyLastLocation() }
        }
    }
}