package com.jhyun.rakuten.databinding


import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter


@BindingAdapter(value = ["note_editable"])
fun EditText.noteEditable(editable: Boolean?) {
    if (editable == true) {
        isFocusable = true
        isFocusableInTouchMode = true
        isCursorVisible = true
    } else {
        isFocusable = false
        isFocusableInTouchMode = false
        isCursorVisible = false
    }

}

@BindingAdapter(value = ["selected"])
fun View.setSelected(selected: Boolean?) {
    selected?.let {
        isSelected = it
    }
}
