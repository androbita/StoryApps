package com.ngopidevteam.storyapps.view.customview

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if (text.toString().isNotEmpty() && text.toString().length < 8){
            setError("Password tidak boleh kurang dari 8 karakter", null)
        }else{
            error = null
        }
    }
}