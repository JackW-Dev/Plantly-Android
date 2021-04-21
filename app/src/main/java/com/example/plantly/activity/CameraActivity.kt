package com.example.plantly.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.plantly.R
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.app_toolbar.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDir: File
    private lateinit var cameraExecutor: ExecutorService

    private val TAG = "Snap"
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    private val REQUEST_CODE_PERMISSIONS = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )

        /*Get the themeKey from shared pref to initialise the correct colour scheme*/

        when (sharedPreferences.getString(themeKey, "Default")) {
            "Default" ->  this.setTheme(R.style.Theme_Plantly)
            "Daisy" ->  this.setTheme(R.style.Theme_Plantly_Daisy)
            "Sakura" ->  this.setTheme(R.style.Theme_Plantly_Sakura)
            "Cantaloupe" ->  this.setTheme(R.style.Theme_Plantly_Cantaloupe)
        }

        setContentView(R.layout.activity_camera)
        setSupportActionBar(app_toolbar)

        app_toolbar.title = "Plantly - Camera"

        snap_btn.setOnClickListener {
            takePhoto()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSIONS)
        }
        outputDir = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    /*Function to check if camera permission is given*/
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            Toast.makeText(this, "Permissions not granted by the user", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    /*Function to initialise and start camera*/
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(camera_preview.createSurfaceProvider())}

            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview,
                    imageCapture)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    /*Function to capture a photo and store it*/
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(outputDir, "PlantImage - " + SimpleDateFormat(FILENAME_FORMAT,
            Locale.UK).format(System.currentTimeMillis()) + ".jpg")

        val outputOpts = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOpts,
            ContextCompat.getMainExecutor(this), object :
                ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    pic.setImageURI(savedUri)
                }
            })
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name) + " - Images").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }
}