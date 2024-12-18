package com.example.animateuploadicon

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var uploadIcon: ImageView
    private lateinit var completeIcon: ImageView
    private lateinit var startUploadButton: Button

    private var uploadAnimator: ObjectAnimator? = null
    private var isUploading = false
    private var isUploadCompleted = false
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // Initialize views
        uploadIcon = findViewById(R.id.uploadIcon)
        completeIcon = findViewById(R.id.completeIcon)
        startUploadButton = findViewById(R.id.startUploadButton)

        // Setup button click listener
        startUploadButton.setOnClickListener {
            if (!isUploading && !isUploadCompleted) {
                startUploadAnimation()
            }
        }
    }

    private fun startUploadAnimation() {
        isUploading = true

        // Create vertical translation animator for upload icon
        uploadAnimator = ObjectAnimator.ofFloat(uploadIcon, "translationY", 0f, -50f, 0f).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator = LinearInterpolator()
            start()
        }

        // Simulate upload process
        CoroutineScope(Dispatchers.Main).launch {
            for (progress in 1..10) {
                delay(500) // Simulate network delay

                // Change container background to indicate progress

                // Complete upload
                if (progress == 10) {
                    completeUpload()
                }
            }
        }
    }

    private fun completeUpload() {
        // Stop upload animation
        uploadAnimator?.cancel()

        // Hide upload icon
        uploadIcon.visibility = View.GONE

        // Show complete icon with fade in
        completeIcon.apply {
            visibility = View.VISIBLE
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(800)
                .setInterpolator(LinearOutSlowInInterpolator())
                .start()
        }


        isUploading = false
        isUploadCompleted = true
    }

    private fun calculateProgressColor(progress: Float): Int {
        // Interpolate between light gray and blue based on progress
        val red = 224 - (progress * 100).toInt()
        val green = 224 - (progress * 100).toInt()
        val blue = 224 + (progress * 31).toInt()
        return Color.rgb(red, green, blue)
    }
}