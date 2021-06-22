package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.util.Log
import android.view.View

class AnimationUtils(private val loadingButton: LoadingButton) {
    private var buttonValueAnimator = ValueAnimator()
    var progress = 0F

    fun loadButtonAnimator() {
        val width = loadingButton.measuredWidth
        buttonValueAnimator = ValueAnimator.ofFloat(0f, width.toFloat()).apply {
            duration = 2000
            repeatCount = 0
            addUpdateListener { valueAnimator ->
                progress = valueAnimator.animatedValue as Float
                loadingButton.invalidate()
            }
            //buttonValueAnimator.disableDuringAnimation(loadingButton)
            start()
        }
    }

    private fun ValueAnimator.disableDuringAnimation(view: View) {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }
}