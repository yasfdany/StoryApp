package dev.studiocloud.storyapp.ui.components

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.setPadding
import dev.studiocloud.storyapp.App.Companion.toPx
import dev.studiocloud.storyapp.R

class TextField(context: Context, attrs: AttributeSet?) : LinearLayoutCompat(context, attrs) {
    private val typedArray: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TextField, 0, 0)
    private var hint: String? = typedArray.getString(R.styleable.TextField_android_hint)
    private var inputType: Int? = typedArray.getInt(R.styleable.TextField_android_inputType, 0)
    private var passwordVisibility: Boolean = typedArray.getBoolean(R.styleable.TextField_passwordVisibility, false)
    private val editText = AppCompatEditText(context)
    private val isPasswordField = inputType == 129
    private val imageButton = ImageButton(context)

    init {
        background = getDrawable(context, R.drawable.textfield_background)
        orientation = HORIZONTAL
        setVerticalGravity(Gravity.CENTER_VERTICAL)

        editText.hint = hint
        editText.background = null
        editText.textSize = 14f
        editText.layoutParams = LayoutParams(0,WRAP_CONTENT, 1f)
        editText.inputType = inputType ?: InputType.TYPE_CLASS_TEXT
        editText.setPadding(18.toPx.toInt())
        editText.letterSpacing = 0.08f

        if (isPasswordField){
            editText.typeface = Typeface.DEFAULT
            editText.transformationMethod = if(passwordVisibility) PasswordTransformationMethod.getInstance() else HideReturnsTransformationMethod.getInstance()
        }
        addView(editText)

        if(isPasswordField){
            imageButton.layoutParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            imageButton.setPadding(14.toPx.toInt())
            imageButton.setBackgroundColor(Color.TRANSPARENT)
            imageButton.setImageDrawable(getDrawable(context,R.drawable.ic_round_visibility_off_24))
            imageButton.setColorFilter(getColor(context, R.color.stroke))
            imageButton.setOnClickListener {
                togglePasswordVisibility()
            }
            addView(imageButton)
        }
    }

    private fun togglePasswordVisibility(){
        passwordVisibility = editText.transformationMethod != PasswordTransformationMethod.getInstance()
        editText.transformationMethod = if(passwordVisibility) PasswordTransformationMethod.getInstance() else HideReturnsTransformationMethod.getInstance()
        imageButton.setColorFilter(
            if(passwordVisibility) Color.BLACK
            else getColor(context, R.color.stroke)
        )
        imageButton.setImageDrawable(
            if(passwordVisibility) getDrawable(context,R.drawable.ic_round_visibility_24)
            else getDrawable(context,R.drawable.ic_round_visibility_off_24)
        )
    }

    fun getText(): String {
        return editText.text.toString()
    }
}