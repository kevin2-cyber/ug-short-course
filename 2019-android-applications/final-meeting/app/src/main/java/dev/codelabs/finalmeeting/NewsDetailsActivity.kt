package dev.codelabs.finalmeeting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.codelabs.finalmeeting.data.News

class NewsDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)

        // Get news from intent data
        val news: News? = intent.extras?.getParcelable<News?>("extra_news")
        debugger(news)

        // Handling implicit intents
        if (intent.action == Intent.ACTION_VIEW) {
            // https://ww.google.com/news_url?q=1234567
            debugger(intent.data?.getQueryParameters("q"))
        }
    }
}
