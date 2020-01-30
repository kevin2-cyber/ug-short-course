package io.ugshortcourse.voteme.view

import android.content.Intent
import android.os.Bundle
import io.ugshortcourse.voteme.R
import io.ugshortcourse.voteme.core.*
import io.ugshortcourse.voteme.model.Voter
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginActivity(override val layoutId: Int = R.layout.activity_login) : VoteMeBaseActivity() {

    override fun onViewCreated(instanceState: Bundle?, intent: Intent) {

        //Add click action for the login button
        login_btn.setOnClickListener {
            val votersName = voter_login_name.text.toString()
            val votersId = voter_login_id.text.toString()

            //Check conditions for nullity
            if (votersName.couldBeEmpty()) {
                toast("Please enter your full name")
                voter_login_name.requestFocus()
                return@setOnClickListener
            } else if (votersId.couldBeEmpty()) {
                toast("Please enter your valid voter's id number")
                voter_login_id.requestFocus()
                return@setOnClickListener
            }

            //For now, just navigate to the home screen
            toast("Searching for ID: ${voter_login_id.text.toString()}...")
            firestore.collection(COLLECTION_VOTERS)
                .document(voter_login_id.text.toString())
                .get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        //Obtain the result from the query
                        val result = it.result

                        //Convert the result into a voter object
                        val voter = result?.toObject(Voter::class.java)

                        //Display voter's information to the user
                        voteMeLogger(voter.toString())
                        database.login(voter)
                        intentTo(HomeActivity::class.java)
                        finishAfterTransition()
                    } else {
                        toast("Unable to retrieve ${voter_login_name.text.toString()}'s information")
                    }
                }.addOnFailureListener {
                    voteMeLogger(it.localizedMessage)
                    toast(it.localizedMessage)
                }

        }

        //Check account information for the voter in question
        check_account_btn.setOnClickListener {
            val votersName = voter_login_name.text.toString()
            val votersId = voter_login_id.text.toString()

            //Check conditions for nullity
            if (votersName.couldBeEmpty()) {
                toast("Please enter your full name")
                voter_login_name.requestFocus()
                return@setOnClickListener
            } else if (votersId.couldBeEmpty()) {
                toast("Please enter your valid voter's id number")
                voter_login_id.requestFocus()
                return@setOnClickListener
            }

            //Query for voter with provided details
            queryVotersDetails()
        }

    }

    //Makes a call to the database for the voter's details
    private fun queryVotersDetails() {
        toast("Searching for ID: ${voter_login_id.text.toString()}...")
        firestore.collection(COLLECTION_VOTERS).document(voter_login_id.text.toString())
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    //Obtain the result from the query
                    val result = it.result

                    //Convert the result into a voter object
                    val voter = result?.toObject(Voter::class.java)

                    //Display voter's information to the user
                    toast(voter.toString())
                } else {
                    toast("Unable to retrieve ${voter_login_name.text.toString()}'s information")
                }
            }.addOnFailureListener {
                voteMeLogger(it.localizedMessage)
                toast(it.localizedMessage)
            }
    }

}