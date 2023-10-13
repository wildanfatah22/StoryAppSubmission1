package com.example.storyapp.view

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.R
import com.example.storyapp.data.local.datastore.UserPreferences
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.viewmodel.AddStoryViewModel
import com.example.storyapp.viewmodel.AddStoryViewModelFactory
import com.example.storyapp.viewmodel.UserAuthViewModel
import com.example.storyapp.viewmodel.UserAuthViewModelFactory
import com.google.android.gms.maps.model.LatLng
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var token: String

    private var getFile: File? = null
    private lateinit var fileFinal: File
    private var anyPhoto = false
    private lateinit var currentPhotoPath: String
    private var latlng: LatLng? = null


    private val addStoryViewModel: AddStoryViewModel by lazy {
        ViewModelProvider(this, AddStoryViewModelFactory(this))[AddStoryViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttonClicked()

        supportActionBar?.title = resources.getString(R.string.post_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val preferences = UserPreferences.getInstance(dataStore)
        val userLoginViewModel =
            ViewModelProvider(this, UserAuthViewModelFactory(preferences))[UserAuthViewModel::class.java]

        userLoginViewModel.getToken().observe(this) {
            token = it
        }

        userLoginViewModel.getName().observe(this) {
            binding.tvUser.text = StringBuilder(getString(R.string.btn_post)).append(" ").append(it)
        }

        addStoryViewModel.message.observe(this) {
            showToast(it)
        }

        addStoryViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val address = data.getStringExtra("address")
                    val lat = data.getDoubleExtra("lat", 0.0)
                    val lng = data.getDoubleExtra("lng", 0.0)
                    latlng = LatLng(lat, lng)

                    binding.detailLocation.text = address
                }
            }
        }

    private fun buttonClicked() {
        binding.btnPostStory.setOnClickListener {
            if (getFile == null) {
                showToast(resources.getString(R.string.select_image))
                return@setOnClickListener
            }
            val des = binding.tvDescription.text.toString().trim()
            if (des.isEmpty()) {
                binding.tvDescription.error = resources.getString(R.string.fill_description)
                return@setOnClickListener
            }
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val file = getFile as File
                    var compressedFile: File? = null
                    var compressedFileSize = file.length()

                    while (compressedFileSize > 1 * 1024 * 1024) {
                        compressedFile = withContext(Dispatchers.Default) {
                            Compressor.compress(applicationContext, file)
                        }
                        compressedFileSize = compressedFile.length()
                    }
                    fileFinal = compressedFile ?: file
                }

                val requestImageFile =
                    fileFinal.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    fileFinal.name,
                    requestImageFile
                )
                val desPart = des.toRequestBody("text/plain".toMediaType())
                addStoryViewModel.upload(imageMultipart, desPart, latlng?.latitude, latlng?.longitude, token)
            }
        }

        binding.btnCamera.setOnClickListener {
            takePhoto()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.layoutLocation.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                getString(R.string.package_name),
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            anyPhoto = true
            binding.ivImageUpload.setImageBitmap(result)
            binding.tvDescription.requestFocus()
        }
    }

    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            binding.ivImageUpload.setImageURI(selectedImg)
            binding.tvDescription.requestFocus()
        }
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.select_image))
        launcherIntentGallery.launch(chooser)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(
            this@AddStoryActivity,
            StringBuilder(getString(R.string.message)).append(msg),
            Toast.LENGTH_SHORT
        ).show()

        if (msg == "Story created successfully") {
            val intent = Intent(this@AddStoryActivity,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val FILENAME_FORMAT = "MMddyyyy"
    }
}