@file:Suppress("DEPRECATION")

package dev.studiocloud.storyapp.ui.activities.upload

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.databinding.ActivityUploadBinding
import dev.studiocloud.storyapp.ui.components.OnTextChange
import dev.studiocloud.storyapp.utils.ManagePermissions
import dev.studiocloud.storyapp.utils.Tools
import dev.studiocloud.storyapp.viewModel.StoryViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var managePermissions: ManagePermissions
    private lateinit var progressDialog: ProgressDialog

    private lateinit var viewModelFactory: ViewModelFactory
    private var storyViewModel: StoryViewModel? = null
    private val permissionsRequestCode = 123
    private var selectedImage: Uri? = null
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                selectedImage = fileUri
                binding.ivSelectedImage.setImageURI(fileUri)
                binding.buttonAdd.enable = binding.edAddDescription.getText().isNotEmpty() && selectedImage != null
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

        storyViewModel = obtainStoryViewModel()

        val list = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        managePermissions = ManagePermissions(this,list, permissionsRequestCode)
        setupViews()
    }

    private fun pickImage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions(onGranted = {
                ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            })
    }

    private fun postStory(){
        Tools().hideKeyboard(this)
        progressDialog.show()

        storyViewModel?.postNewStory(
            selectedImage,
            binding.edAddDescription.getText(),
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
            buttonAdd.setOnClickListener { postStory() }
        }
    }
}