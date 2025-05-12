package com.uragiristereo.mikansei.core.ui.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class SelectionAwareEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle,
) : AppCompatEditText(context, attrs, defStyleAttr) {
    private lateinit var selectionChangeListeners: MutableSet<SelectionTextWatcher>

    private fun getOrCreateSelectionChangeListeners(): MutableSet<SelectionTextWatcher> {
        if (!::selectionChangeListeners.isInitialized) {
            selectionChangeListeners = mutableSetOf()
        }
        return selectionChangeListeners
    }

    fun addSelectionTextChangedListener(listener: SelectionTextWatcher) {
        getOrCreateSelectionChangeListeners().add(listener)
        addTextChangedListener(listener)
    }

    fun removeSelectionTextChangedListener(listener: SelectionTextWatcher) {
        getOrCreateSelectionChangeListeners().remove(listener)
        removeTextChangedListener(listener)
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)

        getOrCreateSelectionChangeListeners().forEach { listener ->
            listener.onSelectionChanged(selStart, selEnd)
        }
    }
}

interface SelectionTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {}

    fun onSelectionChanged(selStart: Int, selEnd: Int) {}
}
