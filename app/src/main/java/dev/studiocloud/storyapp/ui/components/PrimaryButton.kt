package dev.studiocloud.storyapp.ui.components

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat.getDrawable
import dev.studiocloud.storyapp.R
import android.animation.AnimatorSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.setPadding
import dev.studiocloud.storyapp.App.Companion.toPx

class PrimaryButton(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs), View.OnTouchListener{

    init {
        setPadding(14.toPx.toInt())
        textAlignment = TEXT_ALIGNMENT_CENTER
        stateListAnimator = null
        background = getDrawable(context, R.drawable.primary_button)
        isAllCaps = false
        setTextColor(Color.WHITE)
        setOnTouchListener(this)
    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        val animationSet = AnimatorSet()
        val rect = Rect(view!!.left, view.top, view.right, view.bottom)

        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val scaleX = ObjectAnimator.ofFloat(this, View.SCALE_X, 1f, 0.96f)
                val scaleY = ObjectAnimator.ofFloat(this, View.SCALE_Y, 1f, 0.96f)
                val rotateY = ObjectAnimator.ofFloat(this, View.ROTATION_Y,
                   if(event.x.toInt() > view.width - (view.width / 3)) 4f
                   else if(event.x.toInt() < view.width / 3) -4f
                   else 0f
                )
                animationSet.playTogether(scaleX, scaleY, rotateY);
                animationSet.start();
                true
            }
            MotionEvent.ACTION_UP -> {
                if (rect.contains(view.left + event.x.toInt(), view.top + event.y.toInt())) {
                    performClick()
                }

                val scaleX = ObjectAnimator.ofFloat(this, View.SCALE_X,1f)
                val scaleY = ObjectAnimator.ofFloat(this, View.SCALE_Y,1f)
                val rotateY = ObjectAnimator.ofFloat(this, View.ROTATION_Y,0f)

                animationSet.playTogether(scaleX, scaleY, rotateY);
                animationSet.start();
                true
            }
            else -> false
        }
    }
}