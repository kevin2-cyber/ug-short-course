package dev.codelabs.firebasetestapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.rd.animation.type.AnimationType
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val adapter = ViewPagerAdapter(this)
        pager.adapter = adapter
        //pager.pageMargin = resources.getDimensionPixelOffset(R.dimen.margin_normal)

        pageIndicatorView.apply {
            count = adapter.count
        }
    }
}

class ViewPagerAdapter(private val context: Context) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = 5

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v: View = getItem(position)
        container.addView(v)
        return v
    }

    private fun getItem(position: Int): View {
        return when (position) {
            0 -> context.layoutInflater.inflate(R.layout.item_page_one, null, false)
            1 -> context.layoutInflater.inflate(R.layout.item_page_two, null, false)
            2 -> context.layoutInflater.inflate(R.layout.item_page_three, null, false)
            3 -> context.layoutInflater.inflate(R.layout.item_page_four, null, false)
            else -> context.layoutInflater.inflate(R.layout.item_page_five, null, false)
        }
    }
}
