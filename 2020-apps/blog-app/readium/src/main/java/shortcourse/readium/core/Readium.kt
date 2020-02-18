package shortcourse.readium.core

import com.bugsnag.android.Bugsnag
import shortcourse.readium.BuildConfig
import sqip.InAppPaymentsSdk

/**
 * [Readium] is the main entry point of the application
 */
class Readium : ReadiumCore() {

    override fun onCreate() {
        super.onCreate()

        // Enable Bugsnag
        Bugsnag.init(this@Readium)

        // In-App payment
        InAppPaymentsSdk.squareApplicationId = BuildConfig.SQUARE_APP_ID
    }

}