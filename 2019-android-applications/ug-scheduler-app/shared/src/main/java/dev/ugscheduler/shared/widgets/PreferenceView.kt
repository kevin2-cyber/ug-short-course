/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.edit
import androidx.core.view.isVisible
import dev.ugscheduler.shared.BuildConfig
import dev.ugscheduler.shared.R
import kotlinx.android.synthetic.main.preference_view.view.*


class PreferenceView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayoutCompat(context, attrs) {

    var title: String? = null
        set(value) {
            field = value

            if (isInEditMode) {
                findViewById<TextView>(R.id.titleView).text = value
            } else {
                titleView.text = value
            }
        }

    var summary: String? = null
        get() {
            val preferences =
                context.getSharedPreferences(BuildConfig.LIBRARY_PACKAGE_NAME, Context.MODE_PRIVATE)
            return preferences.getString(key, "Not found")
        }
        set(value) {
            field = value
            if (key != null && !value.isNullOrEmpty()) {
                val preferences =
                    context.getSharedPreferences(
                        BuildConfig.LIBRARY_PACKAGE_NAME,
                        Context.MODE_PRIVATE
                    )
                preferences.edit {
                    putString(key, findViewById<TextView>(R.id.summaryView).text.toString())
                    apply()
                }
            }

            if (isInEditMode) {
                findViewById<TextView>(R.id.summaryView).run {
                    text = value
                    isVisible = (value?.isNotEmpty() == true)
                }
            } else {
                summaryView.text = value
                summaryView.isVisible = (value?.isNotEmpty() == true)
            }
        }

    var key: String? = null
        set(value) {
            field = value

            val view = findViewById<TextView>(R.id.summaryView)

            if (view.isVisible && !view.text.isNullOrEmpty()) {
                val preferences =
                    context.getSharedPreferences(
                        BuildConfig.LIBRARY_PACKAGE_NAME,
                        Context.MODE_PRIVATE
                    )
                preferences.edit {
                    putString(key, findViewById<TextView>(R.id.summaryView).text.toString())
                    apply()
                }
            }

        }

    @Nullable
    val widget: ViewGroup? = findViewById<ViewGroup>(R.id.widgetFrame)

    var hideWidget: Boolean = false
        set(value) {
            field = value
            with(
                context.getSharedPreferences(
                    BuildConfig.LIBRARY_PACKAGE_NAME,
                    Context.MODE_PRIVATE
                )
            ) {
                edit {
                    putBoolean(key, value)
                    apply()
                }
            }
        }

    init {
        View.inflate(context, R.layout.preference_view, this)
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        context.obtainStyledAttributes(attrs, R.styleable.PreferenceView).run {
            title = getString(R.styleable.PreferenceView_title)
            summary = getString(R.styleable.PreferenceView_summary)
            hideWidget = getBoolean(R.styleable.PreferenceView_hideWidget, false)

            // If there's a custom view used for the preference's widget, inflate it
            getResourceId(R.styleable.PreferenceView_widget, -1).takeIf { it != -1 }?.let { id ->
                if (!hideWidget) View.inflate(context, id, widgetFrame)
            }

            // If an icon is being used, set up the icon view
            getResourceId(R.styleable.PreferenceView_icon, -1).takeIf { it != -1 }?.let { id ->
                icon.isVisible = (true)
                icon.setImageResource(id)
            }

            recycle()
        }
    }

}