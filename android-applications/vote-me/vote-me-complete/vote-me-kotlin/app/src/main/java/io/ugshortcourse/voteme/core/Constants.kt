package io.ugshortcourse.voteme.core

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import io.ugshortcourse.voteme.model.Candidate
import io.ugshortcourse.voteme.model.Voter

//region DATABASE REFERENCES
const val COLLECTION_TOKENS = "registered_tokens"
const val COLLECTION_VOTERS = "voters"
const val COLLECTION_CANDIDATES = "candidates"
const val COLLECTION_CANDIDATE_VOTES = "candidates/%s/votes"
const val DEFAULT_SHARED_PREFS = "vote-me-prefs"
//endregion DATABASE REFERENCES

const val NO_INTERNET_PROMPT = "No internet connection detected"
const val CONNECTED_PROMPT = "Back online"

/**
 * [Category] of electoral [Candidate]
 */
object Category {
    const val PRESIDENT = "President"
    const val VICE = "Vice President"
    const val SECRETARY = "Secretary"
}

/**
 * Checks for the qualification of a [Voter] based on their dues
 */
val Voter.hasGoodStanding: Boolean get() = dues >= 10000L

/**
 * Creates a [HashMap] for any voter model
 */
val Voter.createHashMap: HashMap<String, Any?>
    get() = hashMapOf(
        "key" to key,
        "fullName" to fullName,
        "region" to region,
        "org" to org,
        "timestamp" to timestamp,
        "dues" to dues,
        "voted" to voted
    )

//Shows debug log messages to the console
fun voteMeLogger(msg: Any?) = println("VoteMeLogger: ${msg.toString()}")

/**
 * Create intent from subclass of [VoteMeBaseActivity] to another subclass
 */
fun VoteMeBaseActivity.intentTo(target: Class<out VoteMeBaseActivity>) =
    startActivity(Intent(applicationContext, target))

/**
 * Create intent from subclass of [VoteMeBaseActivity] to another subclass with some data [Bundle]
 */
fun VoteMeBaseActivity.intentWithData(target: Class<out VoteMeBaseActivity>, b: Bundle) {
    val intent = Intent(applicationContext, target)
    intent.putExtras(b)
    startActivity(intent)
}


/**
 * Create intent from a subclass of [VoteMeBaseFragment] to a subclass of [VoteMeBaseActivity]
 */
fun VoteMeBaseFragment.intentTo(target: Class<out VoteMeBaseActivity>) =
    startActivity(Intent(requireActivity().applicationContext, target))

//Add fragment to a layout resource
fun addFragment(fm: FragmentManager, layoutId: Int, fragment: VoteMeBaseFragment) =
    fm.beginTransaction().replace(layoutId, fragment).commit()

//Checks for nullity and emptiness of a character sequence
fun CharSequence?.couldBeEmpty(): Boolean = this != null && length == 0