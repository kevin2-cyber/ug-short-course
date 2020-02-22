package shortcourse.readium.core.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import shortcourse.readium.core.R
import shortcourse.readium.core.glide.GlideApp

/**
 * Glide image binding
 */
@BindingAdapter(value = ["srcUrl", "isCircleCrop"], requireAll = false)
fun bindSrcUrl(view: ImageView, src: String?, circleCrop: Boolean = false) {
    val request = GlideApp.with(view.context)
        .asBitmap()
        .load(src)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .transition(withCrossFade())
    if (circleCrop) request
        .error(R.drawable.ic_avatar)
        .placeholder(R.drawable.ic_avatar)
        .circleCrop().into(view) else
        request.into(view)
}
