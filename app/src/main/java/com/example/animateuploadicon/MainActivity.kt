package com.example.animateuploadicon

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.example.animateuploadicon.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var uploadAnimator: ObjectAnimator? = null
    private var isUploading = false
    private var isUploadCompleted = false
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize views

        // Setup button click listener
        binding.startUploadButton.setOnClickListener {
//            if (!isUploading && !isUploadCompleted) {
                startUploadAnimation()
//            }
        }
    }

    private fun startUploadAnimation() {
        isUploading = true

        // Create vertical translation animator for upload icon
        uploadAnimator = ObjectAnimator.ofFloat(binding.uploadIcon, "translationY", 0f, -100f).apply {
            duration = 1000 // Duration of upward movement
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART // Restart from origin each time
            interpolator = LinearInterpolator()
            start()
        }

        // Simulate upload process
        CoroutineScope(Dispatchers.Main).launch {
            for (progress in 1..10) {
                delay(500) // Simulate network delay
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

        // Fade out upload icon while fading in complete icon
        binding.uploadIcon.animate()
            .alpha(0f)
            .setDuration(500)
            .withEndAction {
                binding.uploadIcon.visibility = View.GONE
                binding.circularYellowRing.visibility = View.GONE
                // Show and fade in complete icon
                binding.completeIcon.visibility = View.VISIBLE

                binding.circularGreenRing.visibility = View.VISIBLE
                val vectorDrawable1 = binding.completeIcon.drawable as AnimatedVectorDrawable
                val vectorDrawable2 = binding.circularGreenRing.drawable as AnimatedVectorDrawable
                vectorDrawable2.start()
                vectorDrawable1.start()
            }
            .start()

    }

    private fun calculateProgressColor(progress: Float): Int {
        // Interpolate between yellow and green based on progress
        val red = 255 - ((progress / 100) * 255).toInt()   // Red component decreases
        val green = 255                            // Green component stays full
        val blue = 0                               // Blue component stays zero
        return Color.rgb(red, green, blue)
    }
}