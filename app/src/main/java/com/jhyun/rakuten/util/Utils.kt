package com.jhyun.rakuten.util

import android.app.Activity
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object Utils {
    fun dpToPx(dp: Float): Int {
        val metrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics).toInt()
    }

    fun hideSoftInputKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            hideSoftInputKeyboard(view)
        }
    }

    fun hideSoftInputKeyboard(view: View) {
        val inputMethodManager = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    fun showSoftInputKeyboard(view: EditText, selectionEnd: Boolean = false) {
        view.requestFocus()

        val inputMethodManager = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        if (selectionEnd) {
            view.setSelection(view.text.length)
        }

    }
}