package io.ugshortcourse.voteme.view

import android.content.Intent
import android.os.Bundle
import io.ugshortcourse.voteme.R
import io.ugshortcourse.voteme.core.Category
import io.ugshortcourse.voteme.core.VoteMeBaseActivity
import io.ugshortcourse.voteme.core.intentWithData
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity(override val layoutId: Int = R.layout.activity_category) : VoteMeBaseActivity() {

    override fun onViewCreated(instanceState: Bundle?, intent: Intent) {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        //Click action for presidential category
        card_presidential.setOnClickListener {
            val b = Bundle(0)
            b.putString(CandidateActivity.EXTRA_CATEGORY_TYPE, Category.PRESIDENT)
            intentWithData(CandidateActivity::class.java, b)
        }

        //Click action for veep category
        card_vice_prez.setOnClickListener {
            val b = Bundle(0)
            b.putString(CandidateActivity.EXTRA_CATEGORY_TYPE, Category.VICE)
            intentWithData(CandidateActivity::class.java, b)
        }

        //Click action forsecretarial category
        card_secretarial.setOnClickListener {
            val b = Bundle(0)
            b.putString(CandidateActivity.EXTRA_CATEGORY_TYPE, Category.SECRETARY)
            intentWithData(CandidateActivity::class.java, b)
        }
    }

}