package com.example.storyapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.example.storyapp.R
import com.google.android.material.textfield.TextInputEditText

class CustomPasswordEditText: TextInputEditText {
    var isPasswordValid: Boolean = false
    

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword()
            }
        })
    }



    private fun validatePassword() {
//        val passwordInputLayout = findTextInputLayoutAncestor()
//        if (password.length < 8) {
//            passwordInputLayout?.error = resources.getString(R.string.password_format)
//        } else {
//            passwordInputLayout?.error = null
//        }

        isPasswordValid = (text?.length ?: 0) >= 8
        error = if (!isPasswordValid) {
            resources.getString(R.string.password_format)
        } else {
            null
        }
    }

}