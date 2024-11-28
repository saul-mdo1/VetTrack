package com.example.vettrack.presentation.utils

import android.icu.text.NumberFormat
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.vettrack.R
import timber.log.Timber


@BindingAdapter("android:visibility")
fun visibility(view: View, visible: Boolean?) = when (visible) {
    true -> view.visibility = View.VISIBLE
    else -> view.visibility = View.GONE
}

@BindingAdapter("android:src")
fun src(view: View, resourceId: Int) {
    val drawable = ResourcesCompat.getDrawable(view.resources, resourceId, null)
    Glide.with(view).load(drawable).into(view as ImageView)
}

@BindingAdapter(
    value = ["imageUrl"]
)
fun imageUrl(
    view: ImageView,
    imageUrl: String?
) {
    try {
        val drawable = ContextCompat.getDrawable(
            view.context,
            R.drawable.no_image
        )?.apply {
            setTint(ContextCompat.getColor(view.context, R.color.md_theme_inversePrimary))
        }

        Glide.with(view).load(imageUrl).placeholder(drawable).into(view)
    } catch (e: Exception) {
        Timber.d("ViewBindingAdapters_TAG: imageUrl: exception: $e")
    }
}

@BindingAdapter(value = ["textHtml"])
fun textHtml(view: TextView, stringId: Int) = try {
    val text = view.context.getString(stringId)
    view.text =
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
} catch (e: Exception) {
    Timber.d("ViewBindingAdapters_TAG: text: ERROR: ${e.message} ")
    val text = view.context.getString(stringId)
    view.text = text
}

@BindingAdapter("formattedCurrency")
fun bindFormattedCurrency(editText: EditText, amount: Double?) {
    if (amount != null) {
        val formatted = NumberFormat.getCurrencyInstance().format(amount)
        editText.setText(formatted)
    }

    editText.addTextChangedListener(object : TextWatcher {
        private var current = ""

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (s.toString() != current) {
                editText.removeTextChangedListener(this)

                // Remove currency symbols and formatting
                val cleanString = s.toString().replace("""[$,.]""".toRegex(), "")
                val parsed = cleanString.toDoubleOrNull() ?: 0.0

                current = NumberFormat.getCurrencyInstance().format(parsed / 100)
                editText.setText(current)
                editText.setSelection(current.length)

                editText.addTextChangedListener(this)
            }
        }
    })
}

@BindingAdapter("formattedWeight")
fun bindFormattedWeight(editText: EditText, weight: Double?) {
    if (weight != null) {
        editText.setText(String.format("%.2f", weight))
    }

    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            // Ensure two decimal places
            val cleanString = s.toString().replace(",", "").toDoubleOrNull() ?: 0.0
            val formattedWeight = String.format("%.2f", cleanString)

            if (s.toString() != formattedWeight) {
                editText.removeTextChangedListener(this)
                editText.setText(formattedWeight)
                editText.setSelection(formattedWeight.length)
                editText.addTextChangedListener(this)
            }
        }
    })
}

