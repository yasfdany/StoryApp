package dev.studiocloud.storyapp.ui.components

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.text.InputType
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import dev.studiocloud.storyapp.App.Companion.toPx
import dev.studiocloud.storyapp.R


class TextField(context: Context, attrs: AttributeSet?) : LinearLayoutCompat(context, attrs) {
    private val typedArray: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TextField, 0, 0)
    private var hint: String? = typedArray.getString(R.styleable.TextField_android_hint)
    private var inputType: Int? = typedArray.getInt(R.styleable.TextField_android_inputType, 0)
    private var passwordVisibility: Boolean = typedArray.getBoolean(R.styleable.TextField_passwordVisibility, false)
    private val editText = AppCompatEditText(context)
    private val isPasswordField = inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD + 1
    private val imageButton = ImageButton(context)
    val errorIndicatorButton = ImageButton(context)
    var errorMessage: String? = null
    set(value){
        field = value
        showErrorButton()
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    private fun showErrorButton(){
        if (errorMessage != null){
            errorIndicatorButton.layoutParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            errorIndicatorButton.setPadding(14.toPx.toInt())
            errorIndicatorButton.setBackgroundColor(Color.TRANSPARENT)
            errorIndicatorButton.setImageDrawable(getDrawable(context,R.drawable.ic_round_error_24))
            errorIndicatorButton.setColorFilter(Color.RED)

            if(!isPasswordField){
                removeView(errorIndicatorButton)
                addView(errorIndicatorButton)
            }
            background = getDrawable(context, R.drawable.textfield_error_background)
        } else {
            if(!isPasswordField) removeView(errorIndicatorButton)
            background = getDrawable(context, R.drawable.textfield_background)
        }
    }

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

        editText.addTextChangedListener {
            if(it != null){
                errorMessage = if (it.length < 6) "error"
                else null

                if (inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS + 1){
                    errorMessage = if (!isValidEmail(it)) "hehe"
                    else null
                }
            }
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