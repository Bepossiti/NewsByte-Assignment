package com.restaurant.foodrecipes


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.util.Pair;
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.restaurant.foodrecipes.R
import android.view.animation.TranslateAnimation
import com.restaurant.foodrecipes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private var dY = 0f
    private var targetY = 0f
    private var initialY = 0f
    var centerY = 0f


    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // activity_main

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.tabView.addTab(binding.tabView.newTab().setIcon(R.drawable.food_stand_ic))
        binding.tabView.addTab(binding.tabView.newTab().setIcon(R.drawable.orders_ic))
        binding.tabView.addTab(binding.tabView.newTab().setIcon(R.drawable.baseline_account_circle_24))
        binding.tabView.addTab(binding.tabView.newTab().setIcon(R.drawable.baseline_account_balance_24))

        var isMovingUp = false
        var isMovingDown = false

        binding.imageView!!.post {
            targetY =
                binding.app.y + binding.app.height.toFloat() - binding.imageView.height + dpToPx(56) // Position imageView just above the bottom of app view
        }

        binding.imageView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dY = view.y - event.rawY
                    initialY = event.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    if (event.rawY < initialY && !isMovingUp) {
                        // Moving Up
                        isMovingUp = true
                        isMovingDown = false

                        MovingUp()

                        view.animate()
                            .translationY(targetY)
                            .setDuration(300)
                            .start()

                    } else if (event.rawY > initialY && !isMovingDown) {
                        // Moving Down
                        isMovingDown = true
                        isMovingUp = false

                        MovingDown()

                        view.animate()
                            .translationY(view.y - dpToPx(56))  // Stay at current Y position
                            .setDuration(300)
                            .start()
                    }
                }
            }
            true
        }


    }


    fun MovingUp() {


        // To open the view with the right animation
        openAnimation(binding.shutterLeftView, binding.shutterRightView)

        // Animate out before setting visibility to GONE
        val animate = TranslateAnimation(0F, 0F, 0F, binding.tabView.height.toFloat())
        animate.duration = 600
        animate.fillAfter = true
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // No action needed
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.tabView.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // No action needed
            }
        })
        binding.tabView.startAnimation(animate)


//        ****************************************************************************************

        // Show view with zoom-in animation from 200% to 100% and fade in from 0% to 100%
        binding.PriceView.visibility = View.VISIBLE

        val fadeInPriceView = ObjectAnimator.ofFloat(binding.PriceView, View.ALPHA, 0f, 1f)
        fadeInPriceView.duration = 850 // Set duration as needed (in milliseconds)

        fadeInPriceView.start()


//****************************************************************************************************

        // Hide view with zoom-out animation from 100% to 200% and fade out from 100% to 0%
        val scaleAnimation = ScaleAnimation(
            1f, 2f,  // Start and end values for the X axis scaling
            1f, 2f,  // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f   // Pivot point of Y scaling
        )

        val alphaAnimation =
            AlphaAnimation(1f, 0f)  // Start and end values for the alpha property

        val animationSet = AnimationSet(true)
        animationSet.addAnimation(scaleAnimation)
        animationSet.addAnimation(alphaAnimation)
        animationSet.duration = 500
        animationSet.fillAfter = true

        binding.imageViewBg.startAnimation(animationSet)


//**********************************************************************************************


// Fade out animation
        val fadeOut = ObjectAnimator.ofFloat(binding.nameView, View.ALPHA, 1f, 0f)
        fadeOut.duration = 350 // Set duration as needed (in milliseconds)

// Listener to set visibility to GONE after animation completes
        fadeOut.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                binding.nameView.visibility = View.GONE
            }
        })

        fadeOut.start()
//**********************************************************************************************


        // Show view with zoom-in animation from 200% to 100% and fade in from 0% to 100%
        binding.descriptionView.visibility = View.VISIBLE

        val fadeOutdescriptionView = ObjectAnimator.ofFloat(binding.descriptionView, View.ALPHA, 0f, 1f)
        fadeOutdescriptionView.duration = 600 // Set duration as needed (in milliseconds)

        fadeOutdescriptionView.start()


    }

    fun MovingDown() {


        // To close the view with the left animation
        closeAnimation(binding.shutterLeftView, binding.shutterRightView)


        // visibility of view
        binding.tabView.visibility = View.VISIBLE
        val animate = TranslateAnimation(0F, 0F, binding.tabView.height.toFloat(), 0F)
        // duration of animation
        animate.duration = 600
        animate.fillAfter = true
        binding.tabView.startAnimation(animate)

        //        ****************************************************************************************


        // Fade out animation
        val animatePriceView = ObjectAnimator.ofFloat(binding.PriceView, View.ALPHA, 1f, 0f)
        animatePriceView.duration = 850 // Set duration as needed (in milliseconds)

        animatePriceView.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                binding.PriceView.visibility = View.GONE
            }
        })

        animatePriceView.start()

//        ****************************************************************************************


        // Show view with zoom-in animation from 200% to 100% and fade in from 0% to 100%
        binding.imageViewBg.visibility = View.VISIBLE

        val scaleAnimation = ScaleAnimation(
            2f, 1f,  // Start and end values for the X axis scaling
            2f, 1f,  // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f   // Pivot point of Y scaling
        )

        val alphaAnimation =
            AlphaAnimation(0f, 1f)  // Start and end values for the alpha property

        val animationSet = AnimationSet(true)
        animationSet.addAnimation(scaleAnimation)
        animationSet.addAnimation(alphaAnimation)
        animationSet.duration = 500
        animationSet.fillAfter = true
        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // No action needed
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.imageViewBg.visibility = View.GONE
                // Reset the scale and alpha to default values after the animation
                binding.imageViewBg.scaleX = 1f
                binding.imageViewBg.scaleY = 1f
                binding.imageViewBg.alpha = 1f
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // No action needed
            }
        })

        binding.imageViewBg.startAnimation(animationSet)


//        ********************************************************************************************

        // Show view with zoom-in animation from 200% to 100% and fade in from 0% to 100%
        binding.nameView.visibility = View.VISIBLE

        val fadeIn = ObjectAnimator.ofFloat(binding.nameView, View.ALPHA, 0f, 1f)
        fadeIn.duration = 350 // Set duration as needed (in milliseconds)

        fadeIn.start()


//        ********************************************************************************************

// Fade out animation
        val fadeOutdescriptionView = ObjectAnimator.ofFloat(binding.descriptionView, View.ALPHA, 1f, 0f)
        fadeOutdescriptionView.duration = 300 // Set duration as needed (in milliseconds)

// Listener to set visibility to GONE after animation completes
        fadeOutdescriptionView.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                binding.descriptionView.visibility = View.GONE
            }
        })

        fadeOutdescriptionView.start()

    }

    fun openAnimation(shutterLeftView: View, shutterRightView: View) {
        // Ensure any previous animations are canceled
        shutterLeftView.animate().cancel()
        shutterRightView.animate().cancel()

        // Calculate the screen width
        val screenWidth = shutterLeftView.rootView.width.toFloat()


        val animLeft = ObjectAnimator.ofFloat(shutterLeftView, "translationX", 0f, -screenWidth)
        animLeft.duration = 850 // Adjust duration as per your preference

        // Animation for shutterRightView (right to left)
        val animRight = ObjectAnimator.ofFloat(shutterRightView, "translationX", 0f, screenWidth)
        animRight.duration = 850 // Adjust duration as per your preference


        // Start animations simultaneously in a single AnimatorSet
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animLeft, animRight)

        // Start the new animation set
        animatorSet.start()
    }


    fun closeAnimation(shutterLeftView: View, shutterRightView: View) {
        // Calculate the screen width
        val screenWidth = shutterLeftView.rootView.width.toFloat()

        // Ensure any previous animations are canceled
        shutterLeftView.animate().cancel()
        shutterRightView.animate().cancel()

        // Animation for shutterLeftView (right to left)
        val animLeft = ObjectAnimator.ofFloat(shutterLeftView, "translationX", -screenWidth, 0f)
        animLeft.duration = 450 // Adjust duration as per your preference
        animLeft.interpolator = LinearInterpolator() //

        // Animation for shutterRightView (left to right)
        val animRight = ObjectAnimator.ofFloat(shutterRightView, "translationX", screenWidth, 0f)
        animRight.duration = 450 // Adjust duration as per your preference
        animRight.interpolator = LinearInterpolator() //

        // Start animations simultaneously in a single AnimatorSet
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animLeft, animRight)
        animatorSet.start()
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            targetY =
                binding.app.y + binding.app.height.toFloat() - binding.imageView.height + dpToPx(56) // Position imageView just above the bottom of app view
        }
    }


    fun dpToPx(dp: Int): Float {
        val density = resources.displayMetrics.density
        return dp * density
    }


}