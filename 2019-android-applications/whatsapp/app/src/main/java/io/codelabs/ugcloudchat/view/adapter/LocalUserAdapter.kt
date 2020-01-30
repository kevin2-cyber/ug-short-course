package io.codelabs.ugcloudchat.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.codelabs.ugcloudchat.R
import io.codelabs.ugcloudchat.model.LocalContact
import io.codelabs.ugcloudchat.util.glide.GlideApp
import io.codelabs.ugcloudchat.util.layoutInflater
import kotlinx.android.synthetic.main.item_chat.view.*

/**
 * Adapter implementation
 */
class LocalUserAdapter(private val listener: OnContactClickListener) :
    RecyclerView.Adapter<LocalUserAdapter.ContactViewHolder>() {
    private val dataset = mutableListOf<LocalContact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            parent.context.layoutInflater.inflate(
                R.layout.item_chat,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val user = dataset[position]
        holder.v.chat_display_name.text = user.displayName
        holder.v.chat_phone_number.text = user.phone

        // Set profile image
        if(!user.photoUri.isNullOrEmpty()) {
            GlideApp.with(holder.v.chat_avatar.context)
                .load(user.photoUri)
                .circleCrop()
                .placeholder(R.drawable.chat_avatar)
                .error(R.drawable.chat_avatar)
                .into(holder.v.chat_avatar)
        }

        // Add click listener
        holder.v.setOnClickListener {
            // Get user's information form the cursor
            listener.onContactClicked(user)
        }
    }

    fun addContacts(users: MutableList<LocalContact>) {
        dataset.clear()
        dataset.addAll(users)
        notifyDataSetChanged()
    }


    inner class ContactViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    interface OnContactClickListener {

        fun onContactClicked(contact: LocalContact)

    }

}