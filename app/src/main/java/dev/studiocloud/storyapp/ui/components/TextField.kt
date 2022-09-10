package dev.studiocloud.storyapp.ui.components

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import dev.studiocloud.storyapp.R

class TextField(context: Context, attrs: AttributeSet?) : LinearLayoutCompat(context, attrs) {
    private val typedArray: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TextField, 0, 0)
    private var hint: String? = typedArray.getString(R.styleable.TextField_android_hint)
    private var inputType: Int? = typedArray.getInt(R.styleable.TextField_android_inputType, 0)

    init {
        background = ContextCompat.getDrawable(context, R.drawable.textfield_background)
        orientation = HORIZONTAL

        val editText = AppCompatEditText(context)
        editText.hint = hint
        editText.background = null
        editText.textSize = 14f
        editText.layoutParams = LayoutParams(MATCH_PARENT,WRAP_CONTENT)
        editText.inputType = inputType ?: InputType.TYPE_CLASS_TEXT
        editText.setSingleLine()
        editText.setPadding(42)
        addView(editText)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }
}