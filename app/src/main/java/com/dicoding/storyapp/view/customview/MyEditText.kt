package com.dicoding.storyapp.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MyEditText (context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        super.onTextChanged(s, start, before, count)
        validatePassword(s.toString())
    }

    private fun validatePassword(password: String) {
        error = if (password.length < 8) {
            "Password tidak boleh kurang dari 8 karakter"
        } else {
            null
        }
    }
}