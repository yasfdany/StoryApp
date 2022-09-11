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
import android.util.Patterns
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import dev.studiocloud.storyapp.App.Companion.toPx
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.utils.Tools

interface OnTextChange{
    fun onChange(text: String);
}

class TextField(context: Context, attrs: AttributeSet?) : LinearLayoutCompat(context, attrs) {
    private val typedArray: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TextField, 0, 0)
    private var hint: String? = typedArray.getString(R.styleable.TextField_android_hint)
    private var inputType: Int = typedArray.getInt(R.styleable.TextField_android_inputType, -1)
    private var passwordVisibility: Boolean = typedArray.getBoolean(R.styleable.TextField_passwordVisibility, false)
    private val editText = AppCompatEditText(context)
    private val isPasswordField = inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD + 1
    private val passwordVisibilityPassword = ImageButton(context)
    private val textFieldContainer = LinearLayoutCompat(context)
    private val errorTextView = TextView(context)
    private var errorMessage: String? = null
    private var onTextChange: OnTextChange? = null;

    fun addOnTextChange(onTextChange: OnTextChange){
        this.onTextChange = onTextChange
    }

    private fun initErrorTextView(){
        errorTextView.textSize = 12f
        errorTextView.setTextColor(Color.RED)
        errorTextView.setPadding(0,6.toPx.toInt(),0,0)
        errorTextView.visibility = GONE
    }

    private fun initTextFieldContainer(){
        textFieldContainer.background = getDrawable(context, R.drawable.textfield_background)
        textFieldContainer.orientation = HORIZONTAL
        textFieldContainer.setVerticalGravity(Gravity.CENTER_VERTICAL)
        textFieldContainer.layoutParams = LayoutParams(MATCH_PARENT,WRAP_CONTENT)
    }

    private fun initEditText(){
        editText.hint = hint
        editText.background = null
        editText.textSize = 14f
        editText.layoutParams = LayoutParams(0,WRAP_CONTENT, 1f)
        editText.inputType = if(inputType > 0) inputType else InputType.TYPE_CLASS_TEXT
        editText.setPadding(18.toPx.toInt())
        editText.letterSpacing = 0.08f
        if (isPasswordField){
            editText.typeface = Typeface.DEFAULT
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        editText.addTextChangedListener {
            if(it != null){
                if (isPasswordField){
                    errorMessage = if (it.length < 6) resources.getString(R.string.password_error)
                    else null
                }

                if (inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS + 1){
                    errorMessage = if (!Tools().isValidEmail(it)) resources.getString(R.string.email_error)
                    else null
                }

                errorTextView.text = errorMessage
            }

            errorTextView.visibility = if(errorMessage != null) VISIBLE else GONE
            if(errorMessage != null) textFieldContainer.background = getDrawable(context, R.drawable.textfield_error_background)
            else textFieldContainer.background =  getDrawable(context, R.drawable.textfield_background)

            onTextChange?.onChange(it.toString())
        }
    }

    private fun initPasswordVisibilityButton(){
        textFieldContainer.addView(editText)
        if(isPasswordField){
            passwordVisibilityPassword.layoutParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            passwordVisibilityPassword.setPadding(14.toPx.toInt())
            passwordVisibilityPassword.setBackgroundColor(Color.TRANSPARENT)
            passwordVisibilityPassword.setImageDrawable(getDrawable(context,R.drawable.ic_round_visibility_off_24))
            passwordVisibilityPassword.setColorFilter(getColor(context, R.color.stroke))
            passwordVisibilityPassword.setOnClickListener {
                togglePasswordVisibility()
            }
            textFieldContainer.addView(passwordVisibilityPassword)
        }
    }

    init {
        orientation = VERTICAL

        initErrorTextView()
        initTextFieldContainer()
        initEditText()
        initPasswordVisibilityButton()

        addView(textFieldContainer)
        addView(errorTextView)
    }

    private fun togglePasswordVisibility(){
        passwordVisibility = editText.transformationMethod != HideReturnsTransformationMethod.getInstance()
        editText.transformationMethod = if(passwordVisibility) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
        passwordVisibilityPassword.setColorFilter(
            if(passwordVisibility) Color.BLACK
            else getColor(context, R.color.stroke)
        )
        passwordVisibilityPassword.setImageDrawable(
            if(passwordVisibility) getDrawable(context,R.drawable.ic_round_visibility_24)
            else getDrawable(context,R.drawable.ic_round_visibility_off_24)
        )
    }

    fun getText(): String {
        return editText.text.toString()
    }
}