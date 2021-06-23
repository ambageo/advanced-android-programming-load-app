package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonText = resources.getString(R.string.button_name)

    private var valueAnimator = ValueAnimator()
    private var progress = 0f

    private val loadingButtonAnimation = AnimationUtils(this)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
        //color = resources.getColor(R.color.colorPrimary, null)
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked ->{
                Log.d("ggg", "button clicked")
                buttonText = resources.getString(R.string.button_loading)
                loadingButtonAnimation.loadButtonAnimator()
                loadingButtonAnimation.loadCircleAnimator()
            }
            ButtonState.Loading ->{
                Log.d("ggg", "downloading...")
            }
            ButtonState.Completed ->{
                Log.d("ggg", "download completed")
                buttonText = resources.getString(R.string.button_complete)
                invalidate()
            }
        }
    }


    init {

    }
    @JvmName("setButtonState1")
    fun setButtonState(btnState: ButtonState) {
        buttonState = btnState
    }

    override fun onDraw(canvas: Canvas) {
        //Log.d("ggg", "onDraw")
        super.onDraw(canvas)

        drawDefaultButton(canvas)

        if (buttonState == ButtonState.Clicked) {
            // Draw the button rectangle
            paint.color = resources.getColor(R.color.colorPrimaryDark, null)
            canvas.drawRect(0f, 0f, loadingButtonAnimation.progress, heightSize.toFloat(), paint)
            paint.color = Color.WHITE
            paint.textSize = 66.0f
            canvas.drawText(
                buttonText,
                (widthSize / 2).toFloat(),
                (heightSize / 2 - (paint.descent() + paint.ascent()) / 2),
                paint
            )

            // Draw the download circle
            paint.color = Color.YELLOW
            canvas.drawArc(
                widthSize.toFloat() - 150f,
                heightSize.toFloat() / 2 - 50f,
                widthSize.toFloat() - 50f,
                heightSize.toFloat() / 2 + 50f,
                0.0f,
                loadingButtonAnimation.angle,
                true,
                paint
            )
        } else if (buttonState == ButtonState.Completed) {
            drawDefaultButton(canvas)
        }

    }

    fun drawDefaultButton(canvas: Canvas){
        paint.color = resources.getColor(R.color.colorPrimary, null)
        canvas.drawRect(0f,0f, widthSize.toFloat(), heightSize.toFloat(), paint)
        paint.color = Color.WHITE
        paint.textSize = 66.0f
        canvas.drawText(buttonText, (widthSize/2).toFloat(), (heightSize/2 - (paint.descent() + paint.ascent()) /2 ), paint)
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}