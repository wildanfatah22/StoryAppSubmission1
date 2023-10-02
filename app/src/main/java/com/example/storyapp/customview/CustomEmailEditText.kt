package com.example.storyapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.example.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class CustomEmailEditText: TextInputEditText {

    var isEmailValid = false
    private lateinit var emailSame: String
    private var isEmailHasTaken = false

    private val emailInputLayout: TextInputLayout? by lazy {
        findTextInputLayoutAncestor()
    }


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
        // Tambahkan TextWatcher untuk validasi format email
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateEmail(s.toString())
                if (isEmailHasTaken) {
                    validateEmailHasTaken()
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

    private fun validateEmail(email: String) {
        val emailInputLayout = findTextInputLayoutAncestor()
        if (!isValidEmail(email)) {
            emailInputLayout?.error = resources.getString(R.string.email_format)
        } else {
            emailInputLayout?.error = null
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val pattern = Pattern.compile(emailPattern)
        return pattern.matcher(email).matches()
    }

    private fun validateEmailHasTaken() {
        error = if (isEmailHasTaken && text.toString().trim() == emailSame) {
            resources.getString(R.string.email_taken)
        } else {
            null
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