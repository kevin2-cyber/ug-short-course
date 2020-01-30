/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.widgets


import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.res.ResourcesCompat
import dev.ugscheduler.shared.R

/**
 * This is like [android.widget.RatingBar],  but looks like SeekBar.
 */
class SimpleRatingBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatSeekBar(context, attrs, defStyleAttr) {

    private val rated = thumb
    private val unrated = ResourcesCompat.getDrawable(
        resources,
        R.drawable.unrated_thumb,
        context.theme
    )

    private var isRated: Boolean = false

    private var onRateListener: OnRateListener? = null

    init {
        super.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    onRateListener?.onRate(progress + 1)
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                thumb = rated
            }
        })
    }

    override fun setProgress(progress: Int) {
        super.setProgress(progress)
        updateIndicator(progress)
    }

    override fun setProgress(progress: Int, animate: Boolean) {
        super.setProgress(progress, animate)
        updateIndicator(progress)
    }

    override fun setOnSeekBarChangeListener(l: OnSeekBarChangeListener?) {
        throw UnsupportedOperationException("Use setOnRateListener")
    }

    fun setOnRateListener(listener: OnRateListener) {
        onRateListener = listener
    }

    private fun updateIndicator(progress: Int) {
        thumb = if (progress == -1) {
            isRated = false
            unrated
        } else {
            isRated = true
            rated
        }
    }

    interface OnRateListener {
        fun onRate(rate: Int)
    }
}
