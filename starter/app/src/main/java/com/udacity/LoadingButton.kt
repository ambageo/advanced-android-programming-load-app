package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

private var defaultButtonColor by Delegates.notNull<Int>()
private var loadingButtonColor by Delegates.notNull<Int>()

@RequiresApi(Build.VERSION_CODES.O)
class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonText = resources.getString(R.string.button_name)

    private val loadingButtonAnimation = AnimationUtils(this)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked ->{
                buttonText = resources.getString(R.string.button_loading)
                loadingButtonAnimation.loadButtonAnimator()
                loadingButtonAnimation.loadCircleAnimator()
            }
            ButtonState.Loading ->{
            }
            ButtonState.Completed ->{
                buttonText = resources.getString(R.string.button_complete)
                invalidate()
            }
        }
    }


    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            defaultButtonColor = getColor(R.styleable.LoadingButton_buttonColor1, 0)
            loadingButtonColor = getColor(R.styleable.LoadingButton_buttonColor2, 0)
        }
    }

    @JvmName("setButtonState1")
    fun setButtonState(btnState: ButtonState) {
        buttonState = btnState
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawDefaultButton(canvas)

        if (buttonState == ButtonState.Clicked) {
            // Draw the button rectangle
            paint.color = loadingButtonColor
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

    private fun drawDefaultButton(canvas: Canvas){
        paint.color = defaultButtonColor
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