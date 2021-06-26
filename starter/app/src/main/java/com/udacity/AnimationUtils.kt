package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import androidx.core.animation.doOnEnd

class AnimationUtils(private val loadingButton: LoadingButton) {
    private var buttonValueAnimator = ValueAnimator()
    private var circleValueAnimator = ValueAnimator()
    var progress = 0F
    var angle = 0f

    fun loadButtonAnimator() {
        val width = loadingButton.measuredWidth
        buttonValueAnimator = ValueAnimator.ofFloat(0f, width.toFloat()).apply {
            duration = 2000
            repeatCount = 0
            addUpdateListener { valueAnimator ->
                progress = valueAnimator.animatedValue as Float
                loadingButton.invalidate()
            }
            start()
        }
    }

    fun loadCircleAnimator() {
        circleValueAnimator = ValueAnimator.ofFloat(0F, 360F).apply {
            duration = 1800
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { valueAnimator ->
                angle = valueAnimator.animatedValue as Float
                loadingButton.invalidate()
            }
            // disable during animation
            //circleValueAnimator.disableDuringAnimation(loadingButton)
            start()
        }
    }

    private fun ValueAnimator.changeButtonState(view: View) {
        addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator?) {
               loadingButton.setButtonState(ButtonState.Completed)
                Log.d("ggg", "animation completed")
            }
        })
    }
}