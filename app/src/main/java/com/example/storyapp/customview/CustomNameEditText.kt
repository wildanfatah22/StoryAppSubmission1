package com.example.storyapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import com.example.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomNameEditText: TextInputEditText, View.OnFocusChangeListener {
    private val nameInputLayout: TextInputLayout? by lazy {
        findTextInputLayoutAncestor()
    }

    var isNameValid = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isNameValid = text.toString().trim().isNotEmpty()
                if (!isNameValid) {
                    nameInputLayout?.error = resources.getString(R.string.name_none)
                } else {
                    nameInputLayout?.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            validateName()
        }
    }

    private fun validateName() {
        isNameValid = text.toString().trim().isNotEmpty()
        if (!isNameValid) {
            nameInputLayout?.error = resources.getString(R.string.name_none)
        } else {
            nameInputLayout?.error = null
        }
    }

    private fun findTextInputLayoutAncestor(): TextInputLayout? {
        var parent = parent
        while (parent != null) {
            if (parent is TextInputLayout) {
                return parent
            }
            parent = parent.parent
        }
        return null
    }

}