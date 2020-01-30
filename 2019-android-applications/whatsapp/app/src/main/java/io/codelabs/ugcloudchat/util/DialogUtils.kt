package io.codelabs.ugcloudchat.util

import android.content.Context
import android.text.InputType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.codelabs.ugcloudchat.R
import kotlinx.android.synthetic.main.dialog_input.view.*

object DialogUtils {

    fun showInputDialog(
        title: String,
        context: Context,
        initialText : String? = "",
        inputType: Int = InputType.TYPE_CLASS_NUMBER,
        callback: Callback<String>
    ) {
        val v = context.layoutInflater.inflate(R.layout.dialog_input, null, false)

        // Get the input field
        v.input_field.inputType = inputType

        // Set initial text
        v.input_field.setText(initialText)

        MaterialAlertDialogBuilder(context).apply {
            setTitle(title)
            setView(v)
            setPositiveButton("Proceed") { dialogInterface, _ ->
                dialogInterface.dismiss()
                callback(v.input_field.text.toString())
            }
            setCancelable(false)
        }.create().apply {
            setCanceledOnTouchOutside(false)
            show()
        }
    }

}