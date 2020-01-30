package dev.codelabs.finalmeeting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.*
import dev.codelabs.finalmeeting.data.News
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_news.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RecyclerView
        news_list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = NewsAdapter().apply {
                submitList(deserializeData("news.json"))
            }
            setHasFixedSize(false)
            itemAnimator = DefaultItemAnimator()
        }
    }

    // ViewHolder
    class NewsViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    // Adapter
    class NewsAdapter : ListAdapter<News, NewsViewHolder>(NEWS_DIFF) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
            return NewsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_news,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
            val newsItem = getItem(position)
            holder.v.news_title.text = newsItem.title
            //holder.v.news_title.text = newsItem.imageUri
            holder.v.news_desc.text = newsItem.desc

            holder.v.setOnClickListener {
//                it.context.sendToBrowser(newsItem.url)
                sendToNewsDetailsPage(newsItem, it.context)
            }
        }

        private fun sendToNewsDetailsPage(newsItem: News, context: Context) {
            val intent = Intent(context, NewsDetailsActivity::class.java)
            intent.putExtras(bundleOf("extra_news" to newsItem))
            context.startActivity(intent)
        }

        private fun Context.sendToBrowser(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(intent)
        }

        companion object {
            private val NEWS_DIFF: DiffUtil.ItemCallback<News> =
                object : DiffUtil.ItemCallback<News>() {
                    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean =
                        oldItem.id == newItem.id

                    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean =
                        oldItem == newItem
                }
        }
    }
}
